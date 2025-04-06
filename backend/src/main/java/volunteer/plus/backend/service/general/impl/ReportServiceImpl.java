package volunteer.plus.backend.service.general.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import volunteer.plus.backend.config.storage.AWSProperties;
import volunteer.plus.backend.domain.dto.ReportDTO;
import volunteer.plus.backend.domain.entity.Attachment;
import volunteer.plus.backend.domain.entity.Report;
import volunteer.plus.backend.domain.enums.AIChatClient;
import volunteer.plus.backend.exceptions.ApiException;
import volunteer.plus.backend.exceptions.ErrorCode;
import volunteer.plus.backend.repository.AttachmentRepository;
import volunteer.plus.backend.repository.LevyRepository;
import volunteer.plus.backend.repository.ReportRepository;
import volunteer.plus.backend.service.email.EmailNotificationBuilderService;
import volunteer.plus.backend.service.general.ReportService;
import volunteer.plus.backend.service.general.S3Service;
import volunteer.plus.backend.util.AIClientProviderUtil;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;
    private final LevyRepository levyRepository;
    private final AttachmentRepository attachmentRepository;
    private final S3Service s3Service;
    private final AWSProperties awsProperties;
    private final EmailNotificationBuilderService emailNotificationBuilderService;
    private final AIClientProviderUtil aiClientProviderUtil;

    @Override
    public List<ReportDTO> getReports(final Set<Long> levyIds) {
        log.info("Getting reports by levy ids or all");

        final List<Report> reports;

        if (levyIds == null) {
            reports = reportRepository.findAll();
        } else {
            reports = reportRepository.findAllByLevyIdIn(levyIds);
        }

        return reports.stream()
                .map(this::getReportDTO)
                .toList();
    }

    @Override
    @Transactional
    public ReportDTO createOrUpdate(final Long reportId, final Long levyId, final String data) {
        if (reportId == null && levyId == null) {
            throw new ApiException(ErrorCode.NO_DATA_FOR_REPORT_OPERATION);
        }

        if (reportId != null) {
            final var report = reportRepository.findById(reportId)
                    .orElseThrow(() -> new ApiException(ErrorCode.REPORT_NOT_FOUND));
            report.setData(data);
            return getReportDTO(reportRepository.save(report));
        } else {
            final var levy = levyRepository.findById(levyId)
                    .orElseThrow(() -> new ApiException(ErrorCode.LEVY_NOT_FOUND));

            if (levy.getReport() != null) {
                throw new ApiException(ErrorCode.LEVY_ALREADY_HAS_A_REPORT);
            }

            final var report = Report.builder()
                    .data(data)
                    .attachments(new ArrayList<>())
                    .build();

            levy.setReport(report);
            report.setLevy(levy);

            final var updatedLevy = levyRepository.save(levy);

            return getReportDTO(updatedLevy.getReport());
        }
    }

    @Override
    @Transactional
    public void deleteAll(final Set<Long> reportIds) {
        final var reports = reportRepository.findAllByIdIn(reportIds);

        if (reports.size() != reportIds.size()) {
            throw new ApiException(ErrorCode.REPORT_NOT_FOUND);
        }

        final var s3Keys = reports.stream()
                .map(Report::getAttachments)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .map(Attachment::getFilepath)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        s3Keys.forEach(s3Key -> s3Service.deleteFile(awsProperties.getReportBucketName(), s3Key));

        reportRepository.deleteAll(reports);
    }

    @Override
    @Transactional
    public ReportDTO addAttachment(final Long reportId, final MultipartFile file) {
        log.info("Add attachment to report");

        final var report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ApiException(ErrorCode.REPORT_NOT_FOUND));

        final var s3Key = s3Service.uploadFile(awsProperties.getReportBucketName(), file);

        final var attachment = Attachment.builder()
                .filename(file.getOriginalFilename())
                .filepath(s3Key)
                .build();

        report.addAttachment(attachment);

        final var savedReport = reportRepository.save(report);

        return getReportDTO(savedReport);
    }

    @Override
    @Transactional
    public ReportDTO removeAttachment(final Long reportId, final Long attachmentId) {
        log.info("Remove attachment from report");

        final var report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ApiException(ErrorCode.REPORT_NOT_FOUND));

        final var attachment = report.getAttachments()
                .stream()
                .filter(a -> Objects.equals(a.getId(), attachmentId))
                .findFirst()
                .orElseThrow(() -> new ApiException(ErrorCode.ATTACHMENT_NOT_FOUND));

        s3Service.deleteFile(awsProperties.getReportBucketName(), attachment.getFilepath());

        report.removeAttachment(attachment);

        final var savedReport = reportRepository.save(report);

        return getReportDTO(savedReport);
    }

    @Override
    public ResponseEntity<byte[]> downloadAttachment(final Long attachmentId) {
        final var attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new ApiException(ErrorCode.ATTACHMENT_NOT_FOUND));

        final byte[] bytes = s3Service.downloadFile(awsProperties.getReportBucketName(), attachment.getFilepath());

        return ResponseEntity.ok()
                .header("content-disposition", "attachment; filename=" + attachment.getFilename())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(bytes);
    }

    @Override
    @Transactional
    public void distribute(final Long reportId) {
        final var report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ApiException(ErrorCode.REPORT_NOT_FOUND));
        emailNotificationBuilderService.createReportEmails(report);
    }

    @Override
    @Transactional
    public void generateReportsAnalysis(final AIChatClient aiChatClient,
                                        final Set<Long> reportIds) {
        final List<Report> reports = reportRepository.findAllById(reportIds);
        emailNotificationBuilderService.createReportAnalysisEmails(aiClientProviderUtil.getChatClient(aiChatClient), reports);
    }

    private ReportDTO getReportDTO(Report report) {
        return ReportDTO.builder()
                .id(report.getId())
                .createDate(report.getCreateDate())
                .updateDate(report.getUpdateDate())
                .data(report.getData())
                .attachments(report.getAttachments() == null ? new ArrayList<>() : mapAttachments(report.getAttachments()))
                .build();
    }

    private List<ReportDTO.AttachmentDTO> mapAttachments(final List<Attachment> attachments) {
        return attachments.stream()
                .map(attachment ->
                        ReportDTO.AttachmentDTO.builder()
                                .id(attachment.getId())
                                .createDate(attachment.getCreateDate())
                                .updateDate(attachment.getUpdateDate())
                                .filename(attachment.getFilename())
                                .filepath(attachment.getFilepath())
                                .build())
                .toList();
    }
}
