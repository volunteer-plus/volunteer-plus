package volunteer.plus.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import volunteer.plus.backend.domain.entity.EmailRecipient;

import java.time.LocalDateTime;
import java.util.Set;

public interface EmailRecipientRepository extends JpaRepository<EmailRecipient, Long> {
    @Transactional
    @Modifying
    @Query("update EmailRecipient r set r.sent = :sent, r.sentDate = :sentDate where r.id in (:ids)")
    void updateSentAndSentDateByIds(@Param(value = "sent") final boolean sent,
                                    @Param(value = "sentDate") final LocalDateTime sentDate,
                                    @Param(value = "ids") final Set<Long> ids);
}
