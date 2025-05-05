package volunteer.plus.backend.service.general.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import volunteer.plus.backend.domain.dto.EmailDTO;
import volunteer.plus.backend.domain.entity.User;
import volunteer.plus.backend.domain.entity.VerificationToken;
import volunteer.plus.backend.exceptions.ApiException;
import volunteer.plus.backend.repository.VerificationTokenRepository;
import volunteer.plus.backend.service.email.EmailSenderService;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VerificationTokenService {

    private final VerificationTokenRepository tokenRepo;
    private final EmailSenderService  emailSenderService;
    private static final int DEFAULT_TOKEN_LENGTH = 30;
    private static final SecureRandom RANDOM = new SecureRandom();
    @Value("${app.url}")
    private String appUrl;

    public VerificationToken createTokenForUser(User user) {

        tokenRepo.deleteByUser(user);
        String token = generateSafeToken(DEFAULT_TOKEN_LENGTH);
        VerificationToken vToken = VerificationToken.builder()
                .user(user)
                .token(token)
                .expiryDate(LocalDateTime.now().plusHours(24))
                .build();
        return tokenRepo.save(vToken);
    }

    public void sendConfirmationEmail(User user, VerificationToken token) {
        String link = appUrl + "/auth/verify?token=" + token.getToken();
        String subject = "Підтвердження реєстрації";
        String body = String.format(
                "Привіт, %s!\n\n" +
                        "Щоб підтвердити реєстрацію, перейдіть за посиланням:\n%s\n\n" +
                        "Токен дійсний 24 години.",
                user.getFirstName(), link
        );

        EmailDTO.EmailRecipientDTO recipient = EmailDTO.EmailRecipientDTO.builder()
                .emailAddress(user.getEmail())
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

    @Transactional
    public void confirmToken(String tokenStr) {
        VerificationToken token = tokenRepo.findByToken(tokenStr)
                .orElseThrow(() -> new ApiException("Невірний або прострочений токен"));
        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new ApiException("Токен прострочено");
        }
        User user = token.getUser();
        user.setEnabled(true);
        tokenRepo.delete(token);
    }

    public static String generateSafeToken(int length) {
        byte[] bytes = new byte[length];
        RANDOM.nextBytes(bytes);
        Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
        return encoder.encodeToString(bytes);
    }

    private String getBaseUrl(HttpServletRequest request) {
        return String.format("%s://%s:%d", request.getScheme(), request.getServerName(), request.getServerPort());
    }

}
