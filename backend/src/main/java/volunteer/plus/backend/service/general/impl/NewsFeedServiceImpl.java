package volunteer.plus.backend.service.general.impl;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import volunteer.plus.backend.config.storage.AWSProperties;
import volunteer.plus.backend.domain.dto.ImageGenerationRequestDTO;
import volunteer.plus.backend.domain.dto.NewsFeedAttachmentDTO;
import volunteer.plus.backend.domain.dto.NewsFeedCommentDTO;
import volunteer.plus.backend.domain.dto.NewsFeedDTO;
import volunteer.plus.backend.domain.dto.ai.news.AINewsFeedResponse;
import volunteer.plus.backend.domain.entity.*;
import volunteer.plus.backend.domain.enums.AIChatClient;
import volunteer.plus.backend.domain.enums.NewsFeedGenerationSource;
import volunteer.plus.backend.exceptions.ApiException;
import volunteer.plus.backend.exceptions.ErrorCode;
import volunteer.plus.backend.repository.NewsFeedAttachmentRepository;
import volunteer.plus.backend.repository.NewsFeedCommentRepository;
import volunteer.plus.backend.repository.NewsFeedRepository;
import volunteer.plus.backend.repository.UserRepository;
import volunteer.plus.backend.service.ai.OpenAIService;
import volunteer.plus.backend.service.general.NewsFeedService;
import volunteer.plus.backend.service.general.S3Service;
import volunteer.plus.backend.util.AIClientProviderUtil;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

@Slf4j
@Service
public class NewsFeedServiceImpl implements NewsFeedService {
    private final NewsFeedRepository newsFeedRepository;
    private final NewsFeedCommentRepository newsFeedCommentRepository;
    private final NewsFeedAttachmentRepository newsFeedAttachmentRepository;
    private final S3Service s3Service;
    private final AWSProperties awsProperties;
    private final UserRepository userRepository;
    private final AIClientProviderUtil aiClientProviderUtil;
    private final NewsFeedService newsFeedService;
    private final Resource newsFeedGenerationPrompt;
    private final OpenAIService openAIService;

    public NewsFeedServiceImpl(final NewsFeedRepository newsFeedRepository,
                               final NewsFeedCommentRepository newsFeedCommentRepository,
                               final NewsFeedAttachmentRepository newsFeedAttachmentRepository,
                               final S3Service s3Service,
                               final AWSProperties awsProperties,
                               final UserRepository userRepository,
                               final AIClientProviderUtil aiClientProviderUtil,
                               final @Lazy NewsFeedService newsFeedService,
                               final @Value("classpath:/prompts/news_feed_generation.txt") Resource newsFeedGenerationPrompt,
                               final OpenAIService openAIService) {
        this.newsFeedRepository = newsFeedRepository;
        this.newsFeedCommentRepository = newsFeedCommentRepository;
        this.newsFeedAttachmentRepository = newsFeedAttachmentRepository;
        this.s3Service = s3Service;
        this.awsProperties = awsProperties;
        this.userRepository = userRepository;
        this.aiClientProviderUtil = aiClientProviderUtil;
        this.newsFeedService = newsFeedService;
        this.newsFeedGenerationPrompt = newsFeedGenerationPrompt;
        this.openAIService = openAIService;
    }

    @Override
    public Page<NewsFeedDTO> getNewsFeeds(final Pageable pageable) {
        log.info("Getting news feeds..");
        return newsFeedRepository.findAll(pageable).map(this::mapToNewsFeedDTO);
    }

    @Override
    public NewsFeedDTO getNewsFeed(final Long newsFeedId) {
        final NewsFeed newsFeed = getNewsFeedFromDB(newsFeedId);
        return mapToNewsFeedDTO(newsFeed);
    }

    @Override
    @Transactional
    public NewsFeedDTO createOrUpdateNewsFeed(final Long userId,
                                              final NewsFeedDTO newsFeedDTO) {
        final User user = getUser(userId);

        final NewsFeed newsFeed;

        if (newsFeedDTO.getId() == null) {
            newsFeed = NewsFeed.builder()
                    .subject(newsFeedDTO.getSubject())
                    .body(newsFeedDTO.getBody())
                    .generationSource(
                            newsFeedDTO.getGenerationSource() == null ?
                                    NewsFeedGenerationSource.USER :
                                    newsFeedDTO.getGenerationSource()
                    )
                    .author(user)
                    .comments(new ArrayList<>())
                    .attachments(new ArrayList<>())
                    .build();
        } else {
            newsFeed = newsFeedRepository.findById(newsFeedDTO.getId())
                    .orElseThrow(() -> new ApiException(ErrorCode.NEWS_FEED_NOT_FOUND));

            // only author can update its news feed
            validateNewsFeedContentChangeAvailability(newsFeed.getAuthor(), userId, ErrorCode.USER_CANNOT_UPDATE_NEWS_FEED_OF_ANOTHER_USER);

            newsFeed.setSubject(newsFeedDTO.getSubject());
            newsFeed.setBody(newsFeedDTO.getBody());
        }

        return saveAndReturnDTO(newsFeed);
    }

    @Override
    @Transactional
    public NewsFeedDTO selectNewsFeedLogo(final Long attachmentId,
                                          final boolean isLogo) {
        final NewsFeedAttachment foundAttachment = newsFeedAttachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new ApiException(ErrorCode.NEWS_FEED_ATTACHMENT_NOT_FOUND));

        final NewsFeed newsFeed = foundAttachment.getNewsFeed();

        if (isLogo) {
            newsFeed.getAttachments().forEach(attach -> attach.setLogo(false));
        }

        foundAttachment.setLogo(isLogo);

        return saveAndReturnDTO(newsFeed);
    }

    @Override
    @Transactional
    public NewsFeedDTO createOrUpdateNewsFeedComment(final Long userId,
                                                     final Long newsFeedId,
                                                     final NewsFeedCommentDTO newsFeedCommentDTO) {
        final User user = getUser(userId);

        final NewsFeed newsFeed = getNewsFeedFromDB(newsFeedId);

        if (newsFeedCommentDTO.getId() == null) {
            final NewsFeedComment comment = NewsFeedComment.builder()
                    .body(newsFeedCommentDTO.getBody())
                    .author(user)
                    .build();

            newsFeed.addComment(comment);
        } else {
            newsFeed.getComments()
                    .stream()
                    .filter(el -> Objects.equals(el.getId(), newsFeedCommentDTO.getId()))
                    .findFirst()
                    .ifPresent(comment -> {
                        validateNewsFeedContentChangeAvailability(comment.getAuthor(), user.getId(), ErrorCode.USER_CANNOT_UPDATE_NEWS_FEED_COMMENT_OF_ANOTHER_USER);
                        comment.setBody(newsFeedCommentDTO.getBody());
                    });
        }

        return saveAndReturnDTO(newsFeed);
    }

    @Override
    @Transactional
    public void deleteNewsFeed(final Long newsFeedId) {
        final NewsFeed newsFeed = getNewsFeedFromDB(newsFeedId);
        newsFeedRepository.delete(newsFeed);
    }

    @Override
    @Transactional
    public NewsFeedDTO deleteNewsFeedComment(final Long userId,
                                             final Long newsFeedId,
                                             final Long newsFeedCommentId) {
        final User user = getUser(userId);

        final NewsFeed newsFeed = getNewsFeedFromDB(newsFeedId);

        final NewsFeedComment comment = newsFeedCommentRepository.findById(newsFeedCommentId)
                .orElseThrow(() -> new ApiException(ErrorCode.NEWS_FEED_COMMENT_NOT_FOUND));

        // only author can update its news feed
        validateNewsFeedContentChangeAvailability(comment.getAuthor(), user.getId(), ErrorCode.USER_CANNOT_UPDATE_NEWS_FEED_COMMENT_OF_ANOTHER_USER);

        newsFeed.removeComment(comment);

        return saveAndReturnDTO(newsFeed);
    }

    @Override
    @Transactional
    public NewsFeedDTO addAttachment(final Long newsFeedId,
                                     final boolean isLogo,
                                     final MultipartFile file) {
        log.info("Add news feed attachment");

        final NewsFeed newsFeed = getNewsFeedFromDB(newsFeedId);

        final var s3Key = s3Service.uploadFile(awsProperties.getReportBucketName(), file);

        final var attachment = NewsFeedAttachment.builder()
                .filename(file.getOriginalFilename())
                .s3Link(s3Key)
                .logo(isLogo)
                .build();

        // if we attach new logo previous should not be a logo
        if (isLogo && newsFeed.getAttachments() != null) {
            newsFeed.getAttachments().forEach(attach -> attach.setLogo(false));
        }

        newsFeed.addAttachment(attachment);

        return saveAndReturnDTO(newsFeed);
    }

    @Override
    @Transactional
    public NewsFeedDTO removeAttachment(final Long newsFeedId,
                                        final Long attachmentId) {
        log.info("Remove news feed attachment");

        final NewsFeed newsFeed = getNewsFeedFromDB(newsFeedId);

        final var attachment = newsFeed.getAttachments()
                .stream()
                .filter(a -> Objects.equals(a.getId(), attachmentId))
                .findFirst()
                .orElseThrow(() -> new ApiException(ErrorCode.ATTACHMENT_NOT_FOUND));

        s3Service.deleteFile(awsProperties.getReportBucketName(), attachment.getS3Link());

        // if removed attachment is logo then set logo to the latest attachment
        if (attachment.isLogo()) {
            newsFeed.getAttachments()
                    .stream()
                    .filter(el -> !Objects.equals(el.getId(), attachment.getId()))
                    .max(Comparator.comparing(BaseEntity::getCreateDate))
                    .ifPresent(attach -> attach.setLogo(true));
        }

        newsFeed.removeAttachment(attachment);

        return saveAndReturnDTO(newsFeed);
    }

    @Override
    @Transactional
    public ResponseEntity<byte[]> downloadAttachment(final Long attachmentId) {
        final var attachment = newsFeedAttachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new ApiException(ErrorCode.ATTACHMENT_NOT_FOUND));

        final byte[] bytes = s3Service.downloadFile(awsProperties.getReportBucketName(), attachment.getS3Link());

        return ResponseEntity.ok()
                .header("content-disposition", "attachment; filename=" + attachment.getFilename())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(bytes);
    }

    @SneakyThrows
    @Override
    @Transactional
    public void generateAINewsFeed(final AIChatClient aiChatClient) {
        final ChatClient chatClient = aiClientProviderUtil.getChatClient(aiChatClient);

        final User user = userRepository.findUserByEmail(aiChatClient.getUserEmail())
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

        log.info("Start of generation AI driven news feed...");

        final AINewsFeedResponse response = chatClient.prompt(newsFeedGenerationPrompt.getContentAsString(Charset.defaultCharset()))
                .call()
                .entity(AINewsFeedResponse.class);

        newsFeedService.generateNewsAINewsFeed(user, response, newsFeedService, openAIService);
    }

    @Override
    @Transactional
    public NewsFeedDTO generateNewsAINewsFeed(final User user,
                                              final AINewsFeedResponse response,
                                              final NewsFeedService newsFeedService,
                                              final OpenAIService openAIService) {
        if (response.getSubject() == null || response.getBody() == null) {
            throw new ApiException(ErrorCode.AI_RESPONSE_IS_EMPTY);
        }

        final NewsFeedDTO savedNewsFeedDTO = newsFeedService.createOrUpdateNewsFeed(
                user.getId(),
                NewsFeedDTO.builder()
                        .subject(response.getSubject())
                        .body(response.getBody())
                        .generationSource(NewsFeedGenerationSource.AI)
                        .build()
        );

        final MultipartFile multipartFile = openAIService.generateImageAsMultipartFile(
                ImageGenerationRequestDTO.builder()
                        .quality("standard")
                        .number(1)
                        .height(1024)
                        .width(1024)
                        .prompt(response.getSubject())
                        .build()
        );

        log.info("Image has been generated for AI drive news feed...");

        return newsFeedService.addAttachment(savedNewsFeedDTO.getId(), true, multipartFile);
    }

    private NewsFeedDTO saveAndReturnDTO(final NewsFeed newsFeed) {
        final NewsFeed savedNewsFeed = newsFeedRepository.save(newsFeed);
        return mapToNewsFeedDTO(savedNewsFeed);
    }

    private NewsFeed getNewsFeedFromDB(final Long newsFeedId) {
        return newsFeedRepository.findById(newsFeedId)
                .orElseThrow(() -> new ApiException(ErrorCode.NEWS_FEED_NOT_FOUND));
    }

    private void validateNewsFeedContentChangeAvailability(final User author,
                                                           final Long userId,
                                                           final ErrorCode errorCode) {
        // only author can update its news feed
        if (author != null && !Objects.equals(userId, author.getId())) {
            throw new ApiException(errorCode);
        }
    }

    private User getUser(final Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
    }

    private NewsFeedDTO mapToNewsFeedDTO(final NewsFeed newsFeed) {
        return NewsFeedDTO.builder()
                .id(newsFeed.getId())
                .createDate(newsFeed.getCreateDate())
                .updateDate(newsFeed.getUpdateDate())
                .subject(newsFeed.getSubject())
                .body(newsFeed.getBody())
                .generationSource(newsFeed.getGenerationSource())
                .authorEmail(newsFeed.getAuthor() == null ? null : newsFeed.getAuthor().getEmail())
                .authorFirstName(newsFeed.getAuthor() == null ? null : newsFeed.getAuthor().getFirstName())
                .authorLastName(newsFeed.getAuthor() == null ? null : newsFeed.getAuthor().getLastName())
                .comments(
                        newsFeed.getComments() == null ?
                                new ArrayList<>() :
                                newsFeed.getComments()
                                        .stream()
                                        .map(this::mapToNewsFeedCommentDTO)
                                        .toList()
                )
                .attachments(
                        newsFeed.getAttachments() == null ?
                                new ArrayList<>() :
                                newsFeed.getAttachments()
                                        .stream()
                                        .map(this::mapToNewsFeedAttachmentDTO)
                                        .toList()
                )
                .build();
    }

    private NewsFeedCommentDTO mapToNewsFeedCommentDTO(final NewsFeedComment newsFeedComment) {
        return NewsFeedCommentDTO.builder()
                .id(newsFeedComment.getId())
                .createDate(newsFeedComment.getCreateDate())
                .updateDate(newsFeedComment.getUpdateDate())
                .body(newsFeedComment.getBody())
                .authorEmail(newsFeedComment.getAuthor() == null ? null : newsFeedComment.getAuthor().getEmail())
                .authorFirstName(newsFeedComment.getAuthor() == null ? null : newsFeedComment.getAuthor().getFirstName())
                .authorLastName(newsFeedComment.getAuthor() == null ? null : newsFeedComment.getAuthor().getLastName())
                .build();
    }

    private NewsFeedAttachmentDTO mapToNewsFeedAttachmentDTO(final NewsFeedAttachment attachment) {
        return NewsFeedAttachmentDTO.builder()
                .id(attachment.getId())
                .createDate(attachment.getCreateDate())
                .updateDate(attachment.getUpdateDate())
                .logo(attachment.isLogo())
                .filename(attachment.getFilename())
                .s3Link(attachment.getS3Link())
                .build();
    }
}
