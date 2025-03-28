package volunteer.plus.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import volunteer.plus.backend.domain.entity.AddRequest;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AddRequestRepository extends JpaRepository<AddRequest, Long> {
    Optional<AddRequest> findByRequestId(String requestId);
    List<AddRequest> findByRequestIdIn(Set<String> requestIds);
    List<AddRequest> findAllByRegimentCode(String regimentCode);
}
