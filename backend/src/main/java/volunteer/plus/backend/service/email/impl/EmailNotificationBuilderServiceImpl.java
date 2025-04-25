package volunteer.plus.backend.service.email.impl;

import lombok.SneakyThrows;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import volunteer.plus.backend.domain.dto.LiqPayCreationDTO;
import volunteer.plus.backend.domain.dto.NewsFeedAttachmentDTO;
import volunteer.plus.backend.domain.dto.NewsFeedDTO;
import volunteer.plus.backend.domain.entity.*;
import volunteer.plus.backend.domain.enums.EmailMessageTag;
import volunteer.plus.backend.domain.enums.EmailStatus;
import volunteer.plus.backend.exceptions.ApiException;
import volunteer.plus.backend.exceptions.ErrorCode;
import volunteer.plus.backend.repository.EmailTemplateRepository;
import volunteer.plus.backend.repository.UserRepository;
import volunteer.plus.backend.service.email.EmailNotificationBuilderService;
import volunteer.plus.backend.util.JacksonUtil;

import java.nio.charset.Charset;
import java.util.*;

@Service
public class EmailNotificationBuilderServiceImpl implements EmailNotificationBuilderService {
    public static final String USER_NAME = "userName";
    private final Resource reportAnalysisResource;
    private final UserRepository userRepository;
    private final EmailTemplateRepository emailTemplateRepository;

    public EmailNotificationBuilderServiceImpl(final @Value("classpath:/prompts/report_analysis.txt") Resource reportAnalysisResource,
                                               final UserRepository userRepository,
                                               final EmailTemplateRepository emailTemplateRepository) {
        this.reportAnalysisResource = reportAnalysisResource;
        this.userRepository = userRepository;
        this.emailTemplateRepository = emailTemplateRepository;
    }

    @Override
    @Transactional
    public void createReportEmails(final Report report) {
        final var emailTemplate = emailTemplateRepository.findByEmailMessageTag(EmailMessageTag.EMAIL_MESSAGE_TAG_1)
                .orElseThrow(() -> new ApiException(ErrorCode.EMAIL_TEMPLATE_NOT_FOUND));
        final var users = userRepository.findAllByEmailNotNullAndPasswordNotNull();

        // in this case we need to generate for each user separate notification
        for (final UserRepository.UserMainDataProjection user : users) {
            final EmailNotification emailNotification = new EmailNotification();
            emailNotification.setSubjectData("");
            emailNotification.setTemplateData(JacksonUtil.serialize(
                    Map.of(USER_NAME, getFullName(user),
                            "data", report.getData())
            ));
            emailNotification.setDeleted(false);
            emailNotification.setDraft(false);
            emailNotification.setStatus(EmailStatus.PENDING);
            emailNotification.setEmailRecipients(new ArrayList<>());
            emailNotification.setEmailAttachments(new ArrayList<>());

            final EmailRecipient emailRecipient = EmailRecipient.builder()
                    .fullName(getFullName(user))
                    .emailAddress(user.getEmail())
                    .toRecipient(true)
                    .build();
            emailNotification.addRecipient(emailRecipient);

            report.getAttachments().forEach(attachment -> {
                final EmailAttachment emailAttachment = EmailAttachment.builder()
                        .filename(attachment.getFilename())
                        .s3Link(attachment.getFilepath())
                        .build();
                emailNotification.addAttachment(emailAttachment);
            });

            emailTemplate.addNotification(emailNotification);
        }

        emailTemplateRepository.save(emailTemplate);
    }

    @SneakyThrows
    @Override
    @Transactional
    public void createReportAnalysisEmails(final ChatClient chatClient,
                                           final List<Report> reports) {
        final var emailTemplate = emailTemplateRepository.findByEmailMessageTag(EmailMessageTag.EMAIL_MESSAGE_TAG_5)
                .orElseThrow(() -> new ApiException(ErrorCode.EMAIL_TEMPLATE_NOT_FOUND));

        final var users = userRepository.findAllByEmailNotNullAndPasswordNotNull();

        if (reports == null) {
            return;
        }

        final List<String> reportDetails = reports.stream()
                .map(Report::getData)
                .filter(Objects::nonNull)
                .toList();

        final String prompt = reportAnalysisResource.getContentAsString(Charset.defaultCharset()) + reportDetails;

        final String response = chatClient.prompt(prompt)
                .call()
                .content();

        // in this case we need to generate for each user separate notification
        for (final UserRepository.UserMainDataProjection user : users) {
            final EmailNotification emailNotification = new EmailNotification();
            emailNotification.setSubjectData("");
            emailNotification.setTemplateData(JacksonUtil.serialize(
                    Map.of(
                            USER_NAME, getFullName(user),
                            "data", response
                    )
            ));

            updateEmailNotification(emailNotification, getFullName(user), user.getEmail());

            reports.stream()
                    .filter(el -> el.getAttachments() != null)
                    .flatMap(el -> el.getAttachments().stream())
                    .forEach(attachment -> {
                        final EmailAttachment emailAttachment = EmailAttachment.builder()
                                .filename(attachment.getFilename())
                                .s3Link(attachment.getFilepath())
                                .build();
                        emailNotification.addAttachment(emailAttachment);
                    });

            emailTemplate.addNotification(emailNotification);
        }

        emailTemplateRepository.save(emailTemplate);
    }

    @Override
    @Transactional
    public void createVolunteerFeedbackEmail(final VolunteerFeedback feedback) {
        final var emailTemplate = emailTemplateRepository.findByEmailMessageTag(EmailMessageTag.EMAIL_MESSAGE_TAG_2)
                .orElseThrow(() -> new ApiException(ErrorCode.EMAIL_TEMPLATE_NOT_FOUND));

        final EmailNotification emailNotification = new EmailNotification();
        emailNotification.setSubjectData(JacksonUtil.serialize(
                Map.of(
                        "name", getFullName(feedback.getUser())
                )
        ));
        emailNotification.setTemplateData(JacksonUtil.serialize(
                Map.of(
                        "score", feedback.getReputationScore().toPlainString(),
                        "text", feedback.getText()
                )
        ));
        emailNotification.setDeleted(false);
        emailNotification.setDraft(false);
        emailNotification.setStatus(EmailStatus.PENDING);
        emailNotification.setEmailRecipients(new ArrayList<>());
        emailNotification.setEmailAttachments(new ArrayList<>());

        if (feedback.getVolunteer().getUser() != null) {
            final EmailRecipient emailRecipient = EmailRecipient.builder()
                    .fullName(getFullName(feedback.getVolunteer().getUser()))
                    .emailAddress(feedback.getVolunteer().getUser().getEmail())
                    .toRecipient(true)
                    .build();
            emailNotification.addRecipient(emailRecipient);
        }

        emailTemplate.addNotification(emailNotification);

        emailTemplateRepository.save(emailTemplate);
    }

    @Override
    @Transactional
    public void createUserRegistrationEmail(final User user) {
        final var emailTemplate = emailTemplateRepository.findByEmailMessageTag(EmailMessageTag.EMAIL_MESSAGE_TAG_3)
                .orElseThrow(() -> new ApiException(ErrorCode.EMAIL_TEMPLATE_NOT_FOUND));

        final EmailNotification emailNotification = new EmailNotification();
        emailNotification.setSubjectData(JacksonUtil.serialize(Map.of("name", getFullName(user))));
        emailNotification.setTemplateData(JacksonUtil.serialize(Map.of()));
        updateEmailNotification(emailNotification, getFullName(user), user.getEmail());

        emailTemplate.addNotification(emailNotification);

        emailTemplateRepository.save(emailTemplate);
    }

    @Override
    @Transactional
    public void createUserPaymentEmail(final LiqPayCreationDTO liqPayCreationDTO,
                                       final User user) {
        final var emailTemplate = emailTemplateRepository.findByEmailMessageTag(EmailMessageTag.EMAIL_MESSAGE_TAG_4)
                .orElseThrow(() -> new ApiException(ErrorCode.EMAIL_TEMPLATE_NOT_FOUND));

        final EmailNotification emailNotification = new EmailNotification();
        emailNotification.setSubjectData(JacksonUtil.serialize(Map.of("name", getFullName(user))));
        emailNotification.setTemplateData(JacksonUtil.serialize(Map.of("amount", liqPayCreationDTO.getAmount())));
        updateEmailNotification(emailNotification, getFullName(user), user.getEmail());

        emailTemplate.addNotification(emailNotification);

        emailTemplateRepository.save(emailTemplate);
    }

    @Override
    @Transactional
    public void createNewsFeedAIEmailNotification(final NewsFeedDTO newsFeedDTO,
                                                  final EmailMessageTag messageTag) {
        final var emailTemplate = emailTemplateRepository.findByEmailMessageTag(messageTag)
                .orElseThrow(() -> new ApiException(ErrorCode.EMAIL_TEMPLATE_NOT_FOUND));

        final var users = userRepository.findAllByEmailNotNullAndPasswordNotNull();

        if (newsFeedDTO == null) {
            return;
        }

        // in this case we need to generate for each user separate notification
        for (final UserRepository.UserMainDataProjection user : users) {
            final EmailNotification emailNotification = new EmailNotification();
            emailNotification.setSubjectData(JacksonUtil.serialize(Map.of("subject", newsFeedDTO.getSubject())));
            emailNotification.setTemplateData(JacksonUtil.serialize(
                    Map.of(
                            USER_NAME, getFullName(user),
                            "data", newsFeedDTO.getBody()
                    )
            ));

            updateEmailNotification(emailNotification, getFullName(user), user.getEmail());

            final Optional<NewsFeedAttachmentDTO> newsFeedAttachmentDTO = newsFeedDTO.getAttachments() == null ?
                    Optional.empty() :
                    newsFeedDTO.getAttachments()
                            .stream()
                            .filter(newsFeed -> newsFeed.isLogo() && newsFeed.getFilename() != null && newsFeed.getS3Link() != null)
                            .findFirst();

            newsFeedAttachmentDTO.ifPresent(newsFeedAttachment -> {
                final EmailAttachment emailAttachment = EmailAttachment.builder()
                        .filename(newsFeedAttachment.getFilename())
                        .s3Link(newsFeedAttachment.getS3Link())
                        .build();
                emailNotification.addAttachment(emailAttachment);
            });

            emailTemplate.addNotification(emailNotification);
        }

        emailTemplateRepository.save(emailTemplate);
    }

    private void updateEmailNotification(final EmailNotification emailNotification,
                                         final String fullName,
                                         final String email) {
        emailNotification.setDeleted(false);
        emailNotification.setDraft(false);
        emailNotification.setStatus(EmailStatus.PENDING);
        emailNotification.setEmailRecipients(new ArrayList<>());
        emailNotification.setEmailAttachments(new ArrayList<>());

        final EmailRecipient emailRecipient = EmailRecipient.builder()
                .fullName(fullName)
                .emailAddress(email)
                .toRecipient(true)
                .build();

        emailNotification.addRecipient(emailRecipient);
    }

    private String getFullName(final UserRepository.UserMainDataProjection user) {
        if (user.getFirstName() == null && user.getLastName() == null) {
            return "";
        }
        return (user.getFirstName() == null ? "" : user.getFirstName() + " ") + (user.getLastName() == null ? "" : user.getLastName());
    }

    private String getFullName(final User user) {
        if (user.getFirstName() == null && user.getLastName() == null) {
            return "";
        }
        return (user.getFirstName() == null ? "" : user.getFirstName() + " ") + (user.getLastName() == null ? "" : user.getLastName());
    }
}
