package volunteer.plus.backend.api;

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
@RequestMapping("/api/no-auth/quartz/jobs")
public class QuartzJobController {
    private final QuartzJobService quartzJobService;

    @GetMapping
    public List<QuartzJobDTO> findAllAvailableJobs() {
        return quartzJobService.findAvailableJobs();
    }

    @PostMapping("/launch")
    public void launchJob(@RequestBody @Valid final QuartzJobDTO quartzJobDTO) {
        quartzJobService.launchJob(quartzJobDTO);
    }

    @PostMapping("/schedule")
    public void scheduleJob(@RequestBody @Valid final QuartzJobDefinitionDTO quartzJobDefinitionDTO) {
        quartzJobService.scheduleJob(quartzJobDefinitionDTO);
    }

    @GetMapping("/triggers")
    public List<TriggerDefinitionDTO> getTriggersDefinitions(@RequestParam(required = false) final String jobName) {
        return quartzJobService.getTriggerDefinitions(jobName);
    }

    @PutMapping("/trigger/pause/{jobName}")
    public void pauseTrigger(@PathVariable final String jobName) {
        quartzJobService.pauseTrigger(jobName);
    }

    @PutMapping("/trigger/resume/{jobName}")
    public void resumeTrigger(@PathVariable final String jobName) {
        quartzJobService.resumeTrigger(jobName);
    }

    @DeleteMapping("/trigger/delete/{jobName}")
    public void deleteTrigger(@PathVariable final String jobName) {
        quartzJobService.deleteTrigger(jobName);
    }
}
