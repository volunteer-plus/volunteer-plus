package volunteer.plus.backend.service.quartz;

import volunteer.plus.backend.domain.dto.TaskDTO;
import volunteer.plus.backend.domain.dto.TaskDefinitionDTO;
import volunteer.plus.backend.domain.dto.TriggerDefinitionDTO;

import java.util.List;

public interface TaskService {
    List<TaskDTO> findAvailableTasks();

    void launchTask(TaskDTO taskDTO);

    void scheduleTask(TaskDefinitionDTO taskDefinitionDTO);

    List<TriggerDefinitionDTO> getTriggerDefinitions(String taskName);

    void pauseTrigger(String triggerName);

    void resumeTrigger(String triggerName);

    void deleteTrigger(String triggerName);
}
