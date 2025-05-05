package volunteer.plus.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import volunteer.plus.backend.domain.entity.User;
import volunteer.plus.backend.domain.entity.VerificationToken;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    Optional<VerificationToken> findByToken(String token);
    void deleteByUser(User user);

    List<VerificationToken> findAllByExpiryDateBefore(LocalDateTime dateTime);
}
