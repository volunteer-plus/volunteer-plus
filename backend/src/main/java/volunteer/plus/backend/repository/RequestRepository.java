package volunteer.plus.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import volunteer.plus.backend.domain.entity.Request;

public interface RequestRepository extends JpaRepository<Request, Long> {
}
