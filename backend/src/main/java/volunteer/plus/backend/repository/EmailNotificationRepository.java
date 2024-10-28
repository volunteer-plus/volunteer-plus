package volunteer.plus.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import volunteer.plus.backend.domain.entity.EmailNotification;

import java.util.Set;

public interface EmailNotificationRepository extends JpaRepository<EmailNotification, Long> {
    @Query("from EmailNotification e left join fetch e.emailRecipients r " +
            "where e.emailRecipients is not empty " +
            "and (r.sent = false or r.sent is null) " +
            "and e.deleted = false " +
            "and e.draft = false")
    Set<EmailNotification> findEmailNotificationsByNotSentRecipient();
}
