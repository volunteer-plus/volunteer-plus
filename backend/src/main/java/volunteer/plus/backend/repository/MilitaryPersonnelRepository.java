package volunteer.plus.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import volunteer.plus.backend.domain.entity.MilitaryPersonnel;

public interface MilitaryPersonnelRepository extends JpaRepository<MilitaryPersonnel, Long> {
}
