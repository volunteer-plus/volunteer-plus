package volunteer.plus.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import volunteer.plus.backend.domain.entity.Levy;

import java.util.Set;


public interface LevyRepository extends JpaRepository<Levy, Long> {
    Page<Levy> findAllByRequestIdIn(Set<Long> requestIds, Pageable pageable);
}
