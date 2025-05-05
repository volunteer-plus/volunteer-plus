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
import volunteer.plus.backend.domain.entity.VerificationToken;
import volunteer.plus.backend.exceptions.ApiException;
import volunteer.plus.backend.service.auth.AuthService;
import volunteer.plus.backend.service.email.EmailSenderService;
import volunteer.plus.backend.service.general.UserService;
import volunteer.plus.backend.service.general.impl.VerificationTokenService;

import java.security.SecureRandom;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final VerificationTokenService tokenService;
    private final EmailSenderService emailSender;

    private static final int DEFAULT_TOKEN_LENGTH = 30;
    private static final SecureRandom RANDOM = new SecureRandom();

    @Override
    @Transactional
    public void registerUser(final RegistrationData registrationData) {
        Optional<User> existingOpt = userService.findByEmail(registrationData.getEmail());
        if (existingOpt.isPresent()) {
            User existing = existingOpt.get();
            if (!existing.isEnabled()) {
                userService.deleteUser(existing);
            }
            throw new ApiException("Користувач з таким e-mail вже існує");
        }

        var user = User
                .builder()
                .enabled(false)
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

        VerificationToken token = tokenService.createTokenForUser(user);
        tokenService.sendConfirmationEmail(user, token);
    }

    @Override
    @Transactional
    public void resetPasswordByEmail(ResetPasswordEmailRequest resetPasswordByEmailRequest, HttpServletRequest request) {
        String email = resetPasswordByEmailRequest.getEmail();
        User user = (User) userService.loadUserByUsername(email);

        String resetToken = generateSafeToken(DEFAULT_TOKEN_LENGTH);

        userService.setPasswordResetToken(user, resetToken);

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
    @Transactional
    public boolean checkResetToken(String resetToken) {
        var passwordResetToken = userService.getResetToken(resetToken);

        return passwordResetToken.getUser() != null && passwordResetToken.getExpiryDate().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() >= System.currentTimeMillis();
    }

    @Override
    @Transactional
    public boolean changePassword(ResetPasswordRequest resetPasswordRequest) {
        var user = userService.getUserByResetToken(resetPasswordRequest.getResetToken());
        if (user == null ) {
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
