package volunteer.plus.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import volunteer.plus.backend.domain.entity.ConversationRoom;

public interface ConversationRoomRepository extends JpaRepository<ConversationRoom, Long> {
}
