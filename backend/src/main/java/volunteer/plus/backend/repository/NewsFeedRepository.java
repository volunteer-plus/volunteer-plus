package volunteer.plus.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import volunteer.plus.backend.domain.entity.NewsFeed;

public interface NewsFeedRepository extends JpaRepository<NewsFeed, Long> {
}
