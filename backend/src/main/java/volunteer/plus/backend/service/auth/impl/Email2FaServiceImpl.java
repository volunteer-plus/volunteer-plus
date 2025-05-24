package volunteer.plus.backend.service.auth.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import volunteer.plus.backend.domain.dto.EmailDTO;
import volunteer.plus.backend.service.auth.Email2FaService;
import volunteer.plus.backend.service.email.EmailSenderService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class Email2FaServiceImpl implements Email2FaService {

    private final EmailSenderService emailSenderService;


    @Override
    public void send2faCode(String email, String code) {
        String subject = "Код для підтвердження аутентифікації";
        String body = "Ваш код " + code;

        EmailDTO.EmailRecipientDTO recipient = EmailDTO.EmailRecipientDTO.builder()
                .emailAddress(email)
                .toRecipient(true)
                .build();
        EmailDTO emailDTO = EmailDTO.builder()
                .subject(subject)
                .body(body)
                .recipients(Collections.singleton(recipient))
                .build();

        List<EmailDTO.FileAttachment> fileAttachments = new ArrayList<>();
        emailDTO.setAttachments(fileAttachments);

        emailDTO.setAttachments(fileAttachments);

        emailSenderService.sendEmail(emailDTO);
    }
}
