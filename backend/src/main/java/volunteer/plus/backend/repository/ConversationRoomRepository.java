package volunteer.plus.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import volunteer.plus.backend.domain.entity.ConversationRoom;

import java.util.List;
import java.util.Optional;

public interface ConversationRoomRepository extends JpaRepository<ConversationRoom, Long> {
    List<ConversationRoom> findAllByDeletedFalse();
    Optional<ConversationRoom> findByIdAndDeletedFalse(Long conversationRoomId);
}
