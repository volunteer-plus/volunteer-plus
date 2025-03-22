package volunteer.plus.backend.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import volunteer.plus.backend.domain.dto.TaskDTO;
import volunteer.plus.backend.domain.dto.TaskDefinitionDTO;
import volunteer.plus.backend.domain.dto.TriggerDefinitionDTO;
import volunteer.plus.backend.service.quartz.TaskService;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    public List<TaskDTO> findAllAvailableTasks() {
        return taskService.findAvailableTasks();
    }

    @PostMapping("/launch")
    public void launchTask(@RequestBody @Valid final TaskDTO taskDTO) {
        taskService.launchTask(taskDTO);
    }

    @PostMapping("/schedule")
    public void scheduleTask(@RequestBody @Valid final TaskDefinitionDTO taskDefinitionDTO) {
        taskService.scheduleTask(taskDefinitionDTO);
    }

    @GetMapping("/triggers")
    public List<TriggerDefinitionDTO> getTriggersDefinitions(@RequestParam(required = false) final String taskName) {
        return taskService.getTriggerDefinitions(taskName);
    }

    @PutMapping("/trigger/pause/{triggerName}")
    public void pauseTrigger(@PathVariable final String triggerName) {
        taskService.pauseTrigger(triggerName);
    }

    @PutMapping("/trigger/resume/{triggerName}")
    public void resumeTrigger(@PathVariable final String triggerName) {
        taskService.resumeTrigger(triggerName);
    }

    @DeleteMapping("/trigger/delete/{triggerName}")
    public void deleteTrigger(@PathVariable final String triggerName) {
        taskService.deleteTrigger(triggerName);
    }
}
