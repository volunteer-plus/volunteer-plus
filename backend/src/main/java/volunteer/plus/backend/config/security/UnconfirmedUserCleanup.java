package volunteer.plus.backend.config.security;


import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import volunteer.plus.backend.domain.entity.User;
import volunteer.plus.backend.domain.entity.VerificationToken;
import volunteer.plus.backend.repository.UserRepository;
import volunteer.plus.backend.repository.VerificationTokenRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UnconfirmedUserCleanup {

    private final VerificationTokenRepository tokenRepo;
    private final UserRepository userRepo;

    @Async("taskExecutor")
    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    public void purgeExpiredUnconfirmedUsers() {
        LocalDateTime now = LocalDateTime.now();
        List<VerificationToken> expiredTokens = tokenRepo.findAllByExpiryDateBefore(now);

        expiredTokens.forEach(token -> {
            User user = token.getUser();
            tokenRepo.delete(token);
            userRepo.delete(user);
        });
    }

}
