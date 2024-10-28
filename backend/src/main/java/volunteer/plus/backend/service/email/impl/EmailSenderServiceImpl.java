package volunteer.plus.backend.service.email.impl;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import volunteer.plus.backend.domain.dto.EmailDTO;
import volunteer.plus.backend.exceptions.EmailException;
import volunteer.plus.backend.service.email.EmailSenderService;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailSenderServiceImpl implements EmailSenderService {
    @Value("${email.system}")
    private String systemEmail;

    @Value("${email.system.name}")
    private String systemEmailName;

    @Value("${email.send.enable:true}")
    private boolean enableEmailSending;

    private final JavaMailSender mailSender;

    @SneakyThrows
    @Override
    public void sendEmail(final EmailDTO emailDTO) {
        if (emailDTO.getSubject() == null || emailDTO.getBody() == null) {
            throw new EmailException("Cannot send email because content is empty");
        }

        final var message = mailSender.createMimeMessage();
        final var helper = new MimeMessageHelper(message, true);

        helper.setSubject(emailDTO.getSubject());
        helper.setText(emailDTO.getBody(), true);

        for (final var recipient : emailDTO.getRecipients()) {
            if (recipient.isToRecipient()) {
                helper.addTo(recipient.getEmailAddress());
            }

            if (recipient.isCcRecipient()) {
                helper.addCc(recipient.getEmailAddress());
            }

            if (recipient.isBccRecipient()) {
                helper.setBcc(recipient.getEmailAddress());
            }
        }

        for (final var attachment : emailDTO.getAttachments()) {
            helper.addAttachment(attachment.getName(), new ByteArrayResource(attachment.getContent()));
        }

        helper.setFrom(new InternetAddress(systemEmail, systemEmailName));

        send(message);
    }

    private void send(MimeMessage message) {
        if (enableEmailSending) {
            mailSender.send(message);
        }
    }
}
