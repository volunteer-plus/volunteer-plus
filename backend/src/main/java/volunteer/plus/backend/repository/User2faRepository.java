package volunteer.plus.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import volunteer.plus.backend.domain.entity.User;
import volunteer.plus.backend.domain.entity.User2fa;

import java.util.Optional;

public interface User2faRepository extends JpaRepository<User2fa, Long> {

    Optional<User2fa> findByUser(User user);
    Optional<User2fa> findByVerificationId(String verificationId);

}
