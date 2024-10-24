package volunteer.plus.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import volunteer.plus.backend.domain.entity.Attachment;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
}
