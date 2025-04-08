package volunteer.plus.backend.service.quartz.jobs;

import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import volunteer.plus.backend.domain.entity.Report;
import volunteer.plus.backend.domain.enums.AIChatClient;
import volunteer.plus.backend.repository.ReportRepository;
import volunteer.plus.backend.service.general.ReportService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@DisallowConcurrentExecution
public class ReportAnalysisJob implements Job {
    @Autowired
    private ReportService reportService;
    @Autowired
    private ReportRepository reportRepository;

    @Override
    @Transactional
    public void execute(final JobExecutionContext context) {
        log.info("Executing scheduled job: {}", this.getClass().getSimpleName());

        final List<Report> reportsToAnalyze = reportRepository.findAllByAnalyzedFalse();

        final Set<Long> reportsToAnalyzeIds = reportsToAnalyze
                .stream()
                .map(Report::getId)
                .collect(Collectors.toSet());

        if (reportsToAnalyze.isEmpty()) {
            return;
        }

        log.info("Found {} reports to analyze...", reportsToAnalyzeIds.size());

        reportService.generateReportsAnalysis(AIChatClient.OPENAI_DEFAULT, reportsToAnalyzeIds);

        reportsToAnalyze.forEach(report -> report.setAnalyzed(true));

        reportRepository.saveAll(reportsToAnalyze);

        log.info("Executing scheduled job: {} is finished", this.getClass().getSimpleName());
    }
}
