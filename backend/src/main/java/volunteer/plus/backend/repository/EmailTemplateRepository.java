package volunteer.plus.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import volunteer.plus.backend.domain.entity.EmailTemplate;
import volunteer.plus.backend.domain.enums.EmailMessageTag;

import java.util.Optional;

public interface EmailTemplateRepository extends JpaRepository<EmailTemplate, Long> {
    Optional<EmailTemplate> findByEmailMessageTag(EmailMessageTag emailMessageTag);
}
