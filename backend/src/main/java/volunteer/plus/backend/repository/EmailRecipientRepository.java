package volunteer.plus.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import volunteer.plus.backend.domain.entity.EmailRecipient;

public interface EmailRecipientRepository extends JpaRepository<EmailRecipient, Long> {
}
