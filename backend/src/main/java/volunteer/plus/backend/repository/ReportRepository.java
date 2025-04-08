package volunteer.plus.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import volunteer.plus.backend.domain.entity.Report;

import java.util.List;
import java.util.Set;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findAllByLevyIdIn(Set<Long> ids);
    List<Report> findAllByIdIn(Set<Long> ids);
    List<Report> findAllByAnalyzedFalse();
}
