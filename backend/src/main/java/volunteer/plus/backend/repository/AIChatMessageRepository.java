package volunteer.plus.backend.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import volunteer.plus.backend.domain.entity.AIChatMessage;

import java.util.List;

@Repository
public interface AIChatMessageRepository extends JpaRepository<AIChatMessage, Long> {
    List<AIChatMessage> findByConversationIdOrderByCreateDateAsc(String conversationId, Pageable pageable);

    void deleteByConversationId(String conversationId);
}
