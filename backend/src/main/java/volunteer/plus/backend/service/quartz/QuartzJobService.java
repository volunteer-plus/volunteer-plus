package volunteer.plus.backend.service.quartz;

import volunteer.plus.backend.domain.dto.QuartzJobDTO;
import volunteer.plus.backend.domain.dto.QuartzJobDefinitionDTO;
import volunteer.plus.backend.domain.dto.TriggerDefinitionDTO;

import java.util.List;

public interface QuartzJobService {
    List<QuartzJobDTO> findAvailableJobs();

    void launchJob(QuartzJobDTO quartzJobDTO);

    void scheduleJob(QuartzJobDefinitionDTO quartzJobDefinitionDTO);

    List<TriggerDefinitionDTO> getTriggerDefinitions(String taskName);

    void pauseTrigger(String jobName);

    void resumeTrigger(String jobName);

    void deleteTrigger(String jobName);
}
