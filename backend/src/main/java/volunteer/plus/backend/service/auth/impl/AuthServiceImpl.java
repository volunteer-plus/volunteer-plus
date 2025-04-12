package volunteer.plus.backend.service.auth.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import volunteer.plus.backend.domain.dto.EmailDTO;
import volunteer.plus.backend.domain.dto.RegistrationData;
import volunteer.plus.backend.domain.dto.ResetPasswordEmailRequest;
import volunteer.plus.backend.domain.dto.ResetPasswordRequest;
import volunteer.plus.backend.domain.entity.User;
import volunteer.plus.backend.service.auth.AuthService;
import volunteer.plus.backend.service.email.EmailSenderService;
import volunteer.plus.backend.service.general.UserService;

import java.security.SecureRandom;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final EmailSenderService emailSender;
    private static final int DEFAULT_TOKEN_LENGTH = 30;
    private static final long DEFAULT_TOKEN_EXPIRATION_TIME = 432000L;
    private static final SecureRandom RANDOM = new SecureRandom();

    @Override
    @Transactional
    public void registerUser(final RegistrationData registrationData) {
        var user = User
                .builder()
                .enabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .email(registrationData.getEmail())
                .password(encodePassword(registrationData.getPassword()))
                .firstName(registrationData.getFirstName())
                .lastName(registrationData.getLastName())
                .middleName(registrationData.getMiddleName())
                .phoneNumber(registrationData.getPhoneNumber())
                .dateOfBirth(registrationData.getDateOfBirth())
                .build();

        userService.createUser(user);
    }

    @Override
    @Transactional
    public void resetPasswordByEmail(ResetPasswordEmailRequest resetPasswordByEmailRequest, HttpServletRequest request) {
        String email = resetPasswordByEmailRequest.getEmail();
        User user = (User) userService.loadUserByUsername(email);

        String resetToken = generateSafeToken(DEFAULT_TOKEN_LENGTH);
        user.setResetToken(resetToken);
        user.setResetTokenExpTime(System.currentTimeMillis() + DEFAULT_TOKEN_EXPIRATION_TIME);

        String baseUrl = getBaseUrl(request);
        String resetUrl = String.format("%s/api/no-auth/resetPassword?resetToken=%s", baseUrl,
                resetToken);

        EmailDTO emailDTO = EmailDTO.builder()
                .subject("Password Reset Request")
                .body("Please click on the following link to reset your password: " + resetUrl)
                .build();

        EmailDTO.EmailRecipientDTO recipient = EmailDTO.EmailRecipientDTO.builder()
                .emailAddress(email)
                .toRecipient(true)
                .build();

        Set<EmailDTO.EmailRecipientDTO> recipients = new HashSet<>();
        recipients.add(recipient);
        emailDTO.setRecipients(recipients);

        List<EmailDTO.FileAttachment> fileAttachments = new ArrayList<>();
        emailDTO.setAttachments(fileAttachments);

        emailSender.sendEmail(emailDTO);
    }

    @Override
    public boolean checkResetToken(String resetToken) {
        var user = userService.getUserByResetToken(resetToken);
        return user != null && user.getResetTokenExpTime() >= System.currentTimeMillis();
    }

    @Override
    @Transactional
    public boolean changePassword(ResetPasswordRequest resetPasswordRequest) {
        var user = userService.getUserByResetToken(resetPasswordRequest.getResetToken());
        if (user == null || user.getResetTokenExpTime() < System.currentTimeMillis()) {
            return false;
        }

        user.setPassword(encodePassword(resetPasswordRequest.getPassword()));
        return true;
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private String getBaseUrl(HttpServletRequest request) {
        return String.format("%s://%s:%d", request.getScheme(), request.getServerName(), request.getServerPort());
    }

    public static String generateSafeToken(int length) {
        byte[] bytes = new byte[length];
        RANDOM.nextBytes(bytes);
        Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
        return encoder.encodeToString(bytes);
    }

}
