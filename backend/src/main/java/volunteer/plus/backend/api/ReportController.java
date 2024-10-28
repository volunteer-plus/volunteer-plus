package volunteer.plus.backend.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import volunteer.plus.backend.domain.dto.ReportDTO;
import volunteer.plus.backend.service.general.ReportService;

import java.util.List;
import java.util.Set;

@Validated
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/reports")
    @Operation(description = "Retrieve reports by levy ids")
    public ResponseEntity<List<ReportDTO>> getReports(@RequestParam(required = false) final Set<Long> levyIds) {
        return ResponseEntity.ok(reportService.getReports(levyIds));
    }

    @PostMapping("/report/create-or-update")
    @Operation(description = "Create or update a report")
    public ResponseEntity<ReportDTO> createOrUpdate(@RequestParam(required = false) final Long reportId,
                                                    @RequestParam(required = false) final Long levyId,
                                                    @RequestBody final String data) {
        return ResponseEntity.ok(reportService.createOrUpdate(reportId, levyId, data));
    }

    @DeleteMapping("/report/delete")
    @Operation(description = "Delete reports")
    public ResponseEntity<Void> deleteAll(@RequestParam final Set<Long> reportIds) {
        reportService.deleteAll(reportIds);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/report/add-attachment")
    @Operation(description = "Add attachment to report")
    public ResponseEntity<ReportDTO> addAttachments(@RequestParam final Long reportId,
                                                    @RequestBody final MultipartFile file) {
        return ResponseEntity.ok(reportService.addAttachment(reportId, file));
    }

    @PostMapping("/report/remove-attachment")
    @Operation(description = "Remove attachment from report")
    public ResponseEntity<ReportDTO> removeAttachments(@RequestParam final Long reportId,
                                                       @RequestParam final Long attachmentId) {
        return ResponseEntity.ok(reportService.removeAttachment(reportId, attachmentId));
    }

    @PostMapping("/report/download-attachment")
    @Operation(description = "Download attachment from report")
    public ResponseEntity<byte[]> downloadAttachments(@RequestParam final Long attachmentId) {
        return reportService.downloadAttachment(attachmentId);
    }
}
