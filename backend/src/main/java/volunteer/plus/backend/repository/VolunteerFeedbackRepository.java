package volunteer.plus.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import volunteer.plus.backend.domain.entity.VolunteerFeedback;

public interface VolunteerFeedbackRepository extends JpaRepository<VolunteerFeedback, Long> {
}
