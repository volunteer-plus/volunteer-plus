package volunteer.plus.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import volunteer.plus.backend.domain.entity.AddRequest;

import java.util.Optional;

public interface AddRequestRepository extends JpaRepository<AddRequest, Long> {
    Optional<AddRequest> findByRequestId(String requestId);
}
