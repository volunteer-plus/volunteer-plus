package volunteer.plus.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import volunteer.plus.backend.domain.Brigade;

import java.util.List;
import java.util.Set;

public interface BrigadeRepository extends JpaRepository<Brigade, Long> {
    List<Brigade> findAllByRegimentCodeIn(Set<String> regimentCodes);
}
