package volunteer.plus.backend.service.general.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import volunteer.plus.backend.domain.dto.NewsFeedCommentDTO;
import volunteer.plus.backend.domain.dto.NewsFeedDTO;
import volunteer.plus.backend.domain.entity.NewsFeed;
import volunteer.plus.backend.domain.entity.NewsFeedComment;
import volunteer.plus.backend.domain.entity.User;
import volunteer.plus.backend.exceptions.ApiException;
import volunteer.plus.backend.exceptions.ErrorCode;
import volunteer.plus.backend.repository.NewsFeedCommentRepository;
import volunteer.plus.backend.repository.NewsFeedRepository;
import volunteer.plus.backend.repository.UserRepository;
import volunteer.plus.backend.service.general.NewsFeedService;

import java.util.ArrayList;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsFeedServiceImpl implements NewsFeedService {
    private final NewsFeedRepository newsFeedRepository;
    private final NewsFeedCommentRepository newsFeedCommentRepository;
    private final UserRepository userRepository;

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
                    .author(user)
                    .comments(new ArrayList<>())
                    .build();
        } else {
            newsFeed = newsFeedRepository.findById(newsFeedDTO.getId())
                    .orElseThrow(() -> new ApiException(ErrorCode.NEWS_FEED_NOT_FOUND));

            // only author can update its news feed
            validateNewsFeedContentChangeAvailability(newsFeed.getAuthor(), userId, ErrorCode.USER_CANNOT_UPDATE_NEWS_FEED_OF_ANOTHER_USER);

            newsFeed.setSubject(newsFeedDTO.getSubject());
            newsFeed.setBody(newsFeedDTO.getBody());
        }

        final NewsFeed savedNewsFeed = newsFeedRepository.save(newsFeed);

        return mapToNewsFeedDTO(savedNewsFeed);
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

        final NewsFeed savedNewsFeed = newsFeedRepository.save(newsFeed);

        return mapToNewsFeedDTO(savedNewsFeed);
    }

    @Override
    @Transactional
    public void deleteNewsFeed(final Long userId,
                               final Long newsFeedId) {
        final User user = getUser(userId);

        final NewsFeed newsFeed = getNewsFeedFromDB(newsFeedId);

        // only author can update its news feed
        validateNewsFeedContentChangeAvailability(newsFeed.getAuthor(), user.getId(), ErrorCode.USER_CANNOT_UPDATE_NEWS_FEED_OF_ANOTHER_USER);

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
}
