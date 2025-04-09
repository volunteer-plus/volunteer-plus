package volunteer.plus.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import volunteer.plus.backend.domain.entity.NewsFeedAttachment;

public interface NewsFeedAttachmentRepository extends JpaRepository<NewsFeedAttachment, Long> {
}
