package volunteer.plus.backend.service.security.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import volunteer.plus.backend.domain.dto.Send2faResponseDto;
import volunteer.plus.backend.domain.dto.User2faConfigDto;
import volunteer.plus.backend.domain.dto.User2faSettingsDto;
import volunteer.plus.backend.domain.dto.Verify2faRequestDto;
import volunteer.plus.backend.domain.entity.User;
import volunteer.plus.backend.domain.entity.User2fa;
import volunteer.plus.backend.domain.enums.TwoFactorProvider;
import volunteer.plus.backend.repository.User2faRepository;
import volunteer.plus.backend.repository.UserRepository;
import volunteer.plus.backend.service.auth.Email2FaService;
import volunteer.plus.backend.service.auth.SmsService;
import volunteer.plus.backend.service.auth.TotpService;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class User2faService {

    private final User2faRepository repo;
    private final UserRepository userRepo;
    private final SmsService smsService;
    private final Email2FaService emailService;
    private final TotpService totpService;

    private static final SecureRandom RANDOM = new SecureRandom();

    @Transactional
    public User2faSettingsDto updateSettings(User user, User2faSettingsDto dto) {
        User2fa u2 = repo.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("2FA не ініціалізовано"));
        u2.setTotalAllowedTimeSec(dto.getTotalAllowedTimeSec());
        u2.setRetryPeriodSec(dto.getRetryPeriodSec());
        u2.setMaxFailures(dto.getMaxFailures());
        repo.save(u2);
        return User2faSettingsDto.fromEntity(u2);
    }

    @Transactional
    public User2faConfigDto configure2fa(User user, User2faConfigDto dto) {
        User2fa u2 = repo.findByUser(user)
                .orElseGet(() -> User2fa.builder().user(user).attemptsCount(5).totalAllowedTimeSec(3600).retryPeriodSec(60).verificationId(user.getEmail()).build());

        u2.setEnabled(dto.isEnabled());
        u2.setMethodType(dto.getMethodType());

        if (!dto.isEnabled()) {
            u2.setToken(null);
            u2.setExpiryDate(null);
            u2.setAttemptsCount(0);
            u2.setLastCodeSentAt(null);
        }
        repo.save(u2);
        return User2faConfigDto.fromEntity(u2);
    }

    @Transactional
    public Send2faResponseDto send2faCode(String username) {
        User user = userRepo.findUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        User2fa u2 = repo.findByUser(user)
                .orElseThrow(() -> new  RuntimeException("Запит нового коду можна не раніше ніж через "));

        LocalDateTime now = LocalDateTime.now();

        if (u2.getLastCodeSentAt() != null
                && u2.getLastCodeSentAt().plusSeconds(u2.getRetryPeriodSec()).isAfter(now)) {
            throw new RuntimeException("Запит нового коду можна не раніше ніж через "
                    + u2.getRetryPeriodSec() + " сек.");
        }

        if (u2.getMethodType() == TwoFactorProvider.TOTP) {
            if (u2.getSecret() == null) {
                String secret = totpService.generateSecret();
                u2.setSecret(secret);
            }
            String uri = totpService.getUri(
                    u2.getSecret(),
                    user.getUsername(),
                    "MyApp"
            );
            u2.setVerificationId(UUID.randomUUID().toString());
            u2.setLastCodeSentAt(now);
            repo.save(u2);
            return Send2faResponseDto.builder()
                    .verificationId(u2.getVerificationId())
                    .otpAuthUri(uri)
                    .build();
        }

        String code = generateSafeToken(6);
        u2.setToken(code);
        u2.setExpiryDate(now.plusSeconds(u2.getTotalAllowedTimeSec()));
        u2.setAttemptsCount(0);
        u2.setLastCodeSentAt(now);
        u2.setVerificationId(UUID.randomUUID().toString());
        u2.setEnabled(true);

        repo.save(u2);

        dispatchCode(user, code, u2.getMethodType(), u2);

        return new Send2faResponseDto(u2.getVerificationId(), null);
    }

    @Transactional
    public void verify2faCode(Verify2faRequestDto req) {
        User2fa u2 = repo.findByVerificationId(req.getVerificationId())
                .orElseThrow(() -> new BadCredentialsException("Невірний ідентифікатор 2FA"));

        LocalDateTime now = LocalDateTime.now();

        if (!u2.getUser().isAccountNonLocked()) {
            throw new LockedException("Користувач заблокований. Запросіть новий код.");
        }

        if (u2.getMethodType() == TwoFactorProvider.TOTP) {
            boolean valid = totpService.verifyCode(u2.getSecret(), req.getCode());
            if (!valid) {
                u2.setAttemptsCount(u2.getAttemptsCount() + 1);
                if (u2.getAttemptsCount() >= u2.getMaxFailures()) {
                    u2.getUser().setAccountNonLocked(false);
                }
                repo.save(u2);
                throw new BadCredentialsException(
                        "Невірний код автентифікатора. Спроб: " + u2.getAttemptsCount());
            }
            u2.setUsed(true);
            u2.setAttemptsCount(0);
            repo.save(u2);
            return;
        }

        boolean expired = u2.getExpiryDate() == null || u2.getExpiryDate().isBefore(now);
        boolean wrong   = !req.getCode().equals(u2.getToken());
        if (expired || wrong) {
            u2.setAttemptsCount(u2.getAttemptsCount() + 1);
            if (u2.getAttemptsCount() >= u2.getMaxFailures()) {
                u2.getUser().setAccountNonLocked(false);
            }
            repo.save(u2);
            throw new BadCredentialsException(
                    expired ? "Код прострочено." : "Невірний код. Спроб: " + u2.getAttemptsCount());
        }

        u2.setUsed(true);
        u2.setAttemptsCount(0);
        repo.save(u2);
    }

    private void dispatchCode(User user, String code, TwoFactorProvider method, User2fa user2fa) {
        switch (method) {
            case SMS:
                String phone = user.getPhoneNumber();
                if (phone == null || phone.isBlank()) {
                    throw new IllegalStateException("У користувача не вказаний номер телефону для SMS 2FA.");
                }
                smsService.send(phone, buildSmsMessage(code, user2fa));
                break;

            case EMAIL:
                String email = user.getEmail();
                if (email == null || email.isBlank()) {
                    throw new IllegalStateException("У користувача не вказана електронна пошта для Email 2FA.");
                }
                emailService.send2faCode(email, code);
                break;
            default:
                throw new UnsupportedOperationException("Невідомий тип 2FA-провайдера: " + method);
        }
    }

    public static String generateSafeToken(int length) {
        byte[] bytes = new byte[length];
        RANDOM.nextBytes(bytes);
        Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
        return encoder.encodeToString(bytes);
    }

    private String buildSmsMessage(String code, User2fa u2) {
        return String.format("Your verification code is %s. It is valid for %d seconds.",
                code,
                u2.getTotalAllowedTimeSec()
        );
    }

}
