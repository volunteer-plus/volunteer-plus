package volunteer.plus.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import volunteer.plus.backend.domain.entity.WSMessage;

public interface WSMessageRepository extends JpaRepository<WSMessage, Long> {
}
