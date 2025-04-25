package volunteer.plus.backend.service.quartz.jobs;

import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import volunteer.plus.backend.domain.enums.AIChatClient;
import volunteer.plus.backend.service.alerts.AlarmNotificationService;

@Slf4j
@Component
@DisallowConcurrentExecution
public class AlarmAnalyticsJob implements Job {
    @Autowired
    private AlarmNotificationService alarmNotificationService;

    @Override
    public void execute(JobExecutionContext context) {
        log.info("Executing scheduled job: {}", this.getClass().getSimpleName());
        try {
            alarmNotificationService.processAlarmHistoryNotification(AIChatClient.OPENAI_MILITARY);
            log.info("Executing scheduled job: {} is finished", this.getClass().getSimpleName());
        } catch (Exception e) {
            log.error("Error executing scheduled job", e);
        }
    }
}
