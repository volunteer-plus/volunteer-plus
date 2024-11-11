package volunteer.plus.backend.service.email.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import volunteer.plus.backend.domain.entity.*;
import volunteer.plus.backend.domain.enums.EmailMessageTag;
import volunteer.plus.backend.domain.enums.EmailStatus;
import volunteer.plus.backend.exceptions.ApiException;
import volunteer.plus.backend.exceptions.ErrorCode;
import volunteer.plus.backend.repository.EmailTemplateRepository;
import volunteer.plus.backend.repository.UserRepository;
import volunteer.plus.backend.service.email.EmailNotificationBuilderService;
import volunteer.plus.backend.util.JacksonUtil;

import java.util.ArrayList;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailNotificationBuilderServiceImpl implements EmailNotificationBuilderService {
    private final UserRepository userRepository;
    private final EmailTemplateRepository emailTemplateRepository;

    @Override
    @Transactional
    public void createReportEmails(final Report report) {
        final var emailTemplate = emailTemplateRepository.findByEmailMessageTag(EmailMessageTag.EMAIL_MESSAGE_TAG_1)
                .orElseThrow(() -> new ApiException(ErrorCode.EMAIL_TEMPLATE_NOT_FOUND));
        final var users = userRepository.findAllByEmailNotNull();

        // in this case we need to generate for each user separate notification
        users.forEach(user -> {
            final EmailNotification emailNotification = new EmailNotification();
            emailNotification.setSubjectData("");
            emailNotification.setTemplateData(JacksonUtil.serialize(
                    Map.of("userName", getFullName(user),
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
        });

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
