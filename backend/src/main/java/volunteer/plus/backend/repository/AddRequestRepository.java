package volunteer.plus.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import volunteer.plus.backend.domain.AddRequest;

public interface AddRequestRepository extends JpaRepository<AddRequest, Long> {
}
