package volunteer.plus.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import volunteer.plus.backend.domain.entity.NewsFeedComment;

public interface NewsFeedCommentRepository extends JpaRepository<NewsFeedComment, Long> {
}
