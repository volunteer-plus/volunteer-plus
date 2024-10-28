package volunteer.plus.backend.service.email.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import volunteer.plus.backend.config.storage.AWSProperties;
import volunteer.plus.backend.domain.dto.EmailDTO;
import volunteer.plus.backend.domain.entity.EmailAttachment;
import volunteer.plus.backend.domain.entity.EmailNotification;
import volunteer.plus.backend.domain.entity.EmailRecipient;
import volunteer.plus.backend.domain.enums.EmailStatus;
import volunteer.plus.backend.exceptions.ApiException;
import volunteer.plus.backend.exceptions.EmailException;
import volunteer.plus.backend.repository.EmailNotificationRepository;
import volunteer.plus.backend.repository.EmailRecipientRepository;
import volunteer.plus.backend.service.email.EmailContentEvaluationService;
import volunteer.plus.backend.service.email.EmailNotificationService;
import volunteer.plus.backend.service.email.EmailSenderService;
import volunteer.plus.backend.service.general.S3Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailNotificationServiceImpl implements EmailNotificationService {
    private final EmailNotificationRepository emailNotificationRepository;
    private final EmailRecipientRepository emailRecipientRepository;
    private final EmailContentEvaluationService emailContentEvaluationService;
    private final EmailSenderService emailSenderService;
    private final AWSProperties awsProperties;
    private final S3Service s3Service;

    @Override
    public void processEmailNotifications() {
        log.info("Start processing emails...");
        final var emailNotifications = emailNotificationRepository.findEmailNotificationsByNotSentRecipient();
        log.info("Found {} notifications to process", emailNotifications.size());

        final List<EmailException> exceptions = processNotifications(emailNotifications);

        log.info("Total processed: {}, success: {}, failures: {}",
                emailNotifications.size(), emailNotifications.size() - exceptions.size(), exceptions.size());

        handleExceptions(exceptions);
    }

    private void handleExceptions(final List<EmailException> exceptions) {
        if (exceptions.isEmpty()) {
            return;
        }

        exceptions.forEach(e -> log.info("Exception: ", e));

        final String msg = exceptions.stream()
                .map(Throwable::getMessage)
                .collect(Collectors.joining("\n "));

        throw new ApiException("Couldn't process " + exceptions.size() + " Errors: " + msg);
    }

    private List<EmailException> processNotifications(final Set<EmailNotification> emailNotifications) {
        final List<EmailException> exceptions = new ArrayList<>();
        for (final var emailNotification : emailNotifications) {
            try {
                process(emailNotification);
            } catch (EmailException e) {
                updateEmailNotificationStatus(emailNotification, EmailStatus.ERROR);
                exceptions.add(e);
            }
        }
        return exceptions;
    }

    private void process(final EmailNotification emailNotification) {
        log.info("Processing notification with id={}", emailNotification.getId());

        final var template = emailNotification.getTemplate();
        if (template == null) {
            throw new EmailException("Can't process email with id=" + emailNotification.getId() + " because no email template");
        }

        final Set<EmailRecipient> recipients = emailNotification.getEmailRecipients()
                .stream()
                .filter(r -> !r.isSent())
                .filter(r -> r.isToRecipient() || r.isCcRecipient() || r.isBccRecipient())
                .collect(Collectors.toSet());

        final Set<EmailDTO.EmailRecipientDTO> emailRecipientDTOS = getEmailRecipientDTOS(recipients);
        final List<EmailDTO.FileAttachment> fileAttachments = getFileAttachments(emailNotification.getId(), emailNotification.getEmailAttachments());
        final String htmlSubject = getHtmlData(template.getSubject(), emailNotification.getSubjectData(), "Cannot process template subject of notification id={}", emailNotification);
        final String htmlBody = getHtmlData(template.getBody(), emailNotification.getTemplateData(), "Cannot process template body of notification id={}", emailNotification);

        final EmailDTO emailDTO = EmailDTO.builder()
                .subject(htmlSubject)
                .body(htmlBody)
                .recipients(emailRecipientDTOS)
                .attachments(fileAttachments)
                .build();

        try {
            emailSenderService.sendEmail(emailDTO);
        } catch (Exception e) {
            throw new EmailException("Exception occurred during email sending process, notification id=" + emailNotification.getId() + " Errors: " + e.getMessage());
        }

        updateAndSaveRecipientStatus(recipients);
        updateEmailNotificationStatus(emailNotification, EmailStatus.SENT);

        log.info("Email notification id={} successfully processed", emailNotification.getId());
    }

    private void updateEmailNotificationStatus(EmailNotification emailNotification, EmailStatus sent) {
        emailNotification.setStatus(sent);
        emailNotificationRepository.saveAndFlush(emailNotification);
    }

    private void updateAndSaveRecipientStatus(final Set<EmailRecipient> recipients) {
        final Set<Long> ids = recipients.stream()
                .map(EmailRecipient::getId)
                .collect(Collectors.toSet());

        log.info("Updating recipients sent status and date");

        emailRecipientRepository.updateSentAndSentDateByIds(true, LocalDateTime.now(), ids);
    }

    private List<EmailDTO.FileAttachment> getFileAttachments(final Long id, final List<EmailAttachment> attachments) {
        try {
            return attachments
                    .stream()
                    .map(attachment -> {
                        // we will use the same bucket for usage simplicity
                        final byte[] content = s3Service.downloadFile(awsProperties.getReportBucketName(), attachment.getS3Link());
                        return EmailDTO.FileAttachment.builder()
                                .name(attachment.getFilename())
                                .content(content)
                                .build();
                    })
                    .toList();
        } catch (Exception e) {
            throw new EmailException("Exception occurred during file attachment retrieving, notification id=" + id + " Error: " + e.getMessage());
        }
    }

    private String getHtmlData(final String templateData,
                               final String emailNotificationData,
                               final String exceptionMessage,
                               final EmailNotification emailNotification) {
        final String htmlSubject;
        try {
            htmlSubject = emailContentEvaluationService.evaluateContent(templateData, emailNotificationData);
        } catch (Exception e) {
            throw new EmailException(exceptionMessage + emailNotification.getId());
        }
        return htmlSubject;
    }

    private Set<EmailDTO.EmailRecipientDTO> getEmailRecipientDTOS(final Set<EmailRecipient> recipients) {
        return recipients
                .stream()
                .map(r ->
                        EmailDTO.EmailRecipientDTO.builder()
                                .emailAddress(r.getEmailAddress())
                                .toRecipient(r.isToRecipient())
                                .ccRecipient(r.isCcRecipient())
                                .bccRecipient(r.isBccRecipient())
                                .build())
                .collect(Collectors.toSet());
    }
}
