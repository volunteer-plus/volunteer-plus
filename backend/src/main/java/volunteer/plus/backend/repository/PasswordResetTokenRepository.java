package volunteer.plus.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import volunteer.plus.backend.domain.entity.PasswordResetToken;
import volunteer.plus.backend.domain.entity.User;
import volunteer.plus.backend.domain.entity.VerificationToken;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("DELETE FROM VerificationToken t WHERE t.user = :user")
    void deleteByUser(@Param("user") User user);

    List<VerificationToken> findAllByExpiryDateBefore(LocalDateTime dateTime);
}
