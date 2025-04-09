package volunteer.plus.backend.service.quartz.jobs;

import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import volunteer.plus.backend.domain.enums.AIChatClient;
import volunteer.plus.backend.service.general.NewsFeedService;

@Slf4j
@Component
@DisallowConcurrentExecution
public class AINewsFeedGenerationJob implements Job {
    @Autowired
    private NewsFeedService newsFeedService;

    @Override
    public void execute(final JobExecutionContext context) {
        log.info("Executing scheduled job: {}", this.getClass().getSimpleName());
        try {
            newsFeedService.generateAINewsFeed(AIChatClient.OPENAI_MILITARY);
            log.info("Executing scheduled job: {} is finished", this.getClass().getSimpleName());
        } catch (Exception e) {
            log.error("Error executing scheduled job", e);
        }
    }
}
