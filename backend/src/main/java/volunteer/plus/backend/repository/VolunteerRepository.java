package volunteer.plus.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import volunteer.plus.backend.domain.entity.Volunteer;

import java.util.Set;

public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {
    Page<Volunteer> findAllByIdIn(Set<Long> volunteerIds, Pageable pageable);
}
