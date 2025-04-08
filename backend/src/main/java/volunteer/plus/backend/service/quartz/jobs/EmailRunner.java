package volunteer.plus.backend.service.quartz.jobs;

import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import volunteer.plus.backend.service.email.EmailNotificationService;

@Slf4j
@Component
@DisallowConcurrentExecution
public class EmailRunner implements Job {

    @Autowired
    private EmailNotificationService emailNotificationService;

    @Override
    public void execute(final JobExecutionContext jobExecutionContext) {
        log.info("Executing scheduled job: {}", this.getClass().getSimpleName());
        try {
            emailNotificationService.processEmailNotifications();
            log.info("Executing scheduled job: {} is finished", this.getClass().getSimpleName());
        } catch (Exception e) {
            log.error("Error executing scheduled job", e);
        }
    }
}
