package volunteer.plus.backend.api;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import volunteer.plus.backend.domain.dto.QuartzJobDTO;
import volunteer.plus.backend.domain.dto.QuartzJobDefinitionDTO;
import volunteer.plus.backend.domain.dto.TriggerDefinitionDTO;
import volunteer.plus.backend.service.quartz.QuartzJobService;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/quartz/jobs")
public class QuartzJobController {
    private final QuartzJobService quartzJobService;

    @GetMapping
    @Operation(description = "Retrieve all available jobs")
    public List<QuartzJobDTO> findAllAvailableJobs() {
        return quartzJobService.findAvailableJobs();
    }

    @PostMapping("/launch")
    @Operation(description = "Launch job")
    public void launchJob(@RequestBody @Valid final QuartzJobDTO quartzJobDTO) {
        quartzJobService.launchJob(quartzJobDTO);
    }

    @PostMapping("/schedule")
    @Operation(description = "Schedule an existing job")
    public void scheduleJob(@RequestBody @Valid final QuartzJobDefinitionDTO quartzJobDefinitionDTO) {
        quartzJobService.scheduleJob(quartzJobDefinitionDTO);
    }

    @GetMapping("/triggers")
    @Operation(description = "Retrieve job triggers")
    public List<TriggerDefinitionDTO> getTriggersDefinitions(@RequestParam(required = false) final String jobName) {
        return quartzJobService.getTriggerDefinitions(jobName);
    }

    @PutMapping("/trigger/pause/{jobName}")
    @Operation(description = "Pause job trigger")
    public void pauseTrigger(@PathVariable final String jobName) {
        quartzJobService.pauseTrigger(jobName);
    }

    @PutMapping("/trigger/resume/{jobName}")
    @Operation(description = "Resume job trigger")
    public void resumeTrigger(@PathVariable final String jobName) {
        quartzJobService.resumeTrigger(jobName);
    }

    @DeleteMapping("/trigger/delete/{jobName}")
    @Operation(description = "Delete job triggers")
    public void deleteTrigger(@PathVariable final String jobName) {
        quartzJobService.deleteTrigger(jobName);
    }
}
