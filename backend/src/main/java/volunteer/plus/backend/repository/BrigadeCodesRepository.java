package volunteer.plus.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import volunteer.plus.backend.domain.BrigadeCodes;

public interface BrigadeCodesRepository extends JpaRepository<BrigadeCodes, Long> {
}
