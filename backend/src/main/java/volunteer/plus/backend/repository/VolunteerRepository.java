package volunteer.plus.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import volunteer.plus.backend.domain.entity.Volunteer;

public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {
}
