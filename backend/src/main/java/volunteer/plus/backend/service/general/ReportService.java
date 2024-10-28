package volunteer.plus.backend.service.general;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import volunteer.plus.backend.domain.dto.ReportDTO;

import java.util.List;
import java.util.Set;

public interface ReportService {
    List<ReportDTO> getReports(Set<Long> levyIds);

    ReportDTO createOrUpdate(Long reportId, Long levyId, String data);

    void deleteAll(Set<Long> reportIds);

    ReportDTO addAttachment(Long reportId, MultipartFile file);

    ReportDTO removeAttachment(Long reportId, Long attachmentId);

    ResponseEntity<byte[]> downloadAttachment(Long attachmentId);
}
