package volunteer.plus.backend.service.quartz.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.quartz.utils.Key;
import org.springframework.transaction.annotation.Transactional;
import volunteer.plus.backend.domain.dto.QuartzJobDTO;
import volunteer.plus.backend.domain.dto.QuartzJobDefinitionDTO;
import volunteer.plus.backend.domain.dto.TriggerDefinitionDTO;
import volunteer.plus.backend.exceptions.ApiException;
import volunteer.plus.backend.service.quartz.QuartzJobService;
import volunteer.plus.backend.util.QuartzUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuartzJobServiceImpl implements QuartzJobService {
    private final ApplicationContext context;
    private final Scheduler scheduler;

    @Override
    public List<QuartzJobDTO> findAvailableJobs() {
        log.info("Getting all quartz jobs...");
        final Map<String, Job> jobs = context.getBeansOfType(Job.class);
        return jobs.keySet()
                .stream()
                .map(job ->
                        QuartzJobDTO.builder()
                                .name(job)
                                .triggers(getTriggerDefinitions(job))
                                .build()
                )
                .toList();
    }

    @SneakyThrows
    @Override
    public void launchJob(final QuartzJobDTO quartzJobDTO) {
        log.info("Launching job {}", quartzJobDTO);

        final Class<? extends Job> jobClass = getJobTargetClass(quartzJobDTO.getName());

        JobDetail detail = findDetailByJobName(quartzJobDTO.getName());

        if (detail == null) {
            detail = createNewJobDetail(quartzJobDTO.getName(), jobClass);
        }

        scheduler.triggerJob(detail.getKey());
    }

    @SneakyThrows
    @Override
    @Transactional
    public void scheduleJob(final QuartzJobDefinitionDTO quartzJobDefinitionDTO) {
        log.info("Scheduling job {}", quartzJobDefinitionDTO);

        final Class<? extends Job> jobClass = getJobTargetClass(quartzJobDefinitionDTO.getJobName());

        JobDetail detail = findDetailByJobName(quartzJobDefinitionDTO.getJobName());

        if (detail == null) {
            detail = createNewJobDetail(quartzJobDefinitionDTO.getJobName(), jobClass);
        }

        final CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                .withIdentity(quartzJobDefinitionDTO.getTrigger().getName(), Key.DEFAULT_GROUP)
                .withSchedule(CronScheduleBuilder.cronSchedule(quartzJobDefinitionDTO.getTrigger().getCronExpression()))
                .forJob(detail.getKey())
                .build();

        if (scheduler.checkExists(cronTrigger.getKey())) {
            throw new ApiException("Cron key already exists");
        }

        for (final Trigger trigger : scheduler.getTriggersOfJob(detail.getKey())) {
            if (trigger instanceof CronTrigger cron && quartzJobDefinitionDTO.getTrigger().getCronExpression().equalsIgnoreCase(cron.getCronExpression())) {
                throw new ApiException("Trigger with cron " + cron.getCronExpression() + " already exists");
            }
        }

        scheduler.scheduleJob(cronTrigger);
    }

    @SneakyThrows
    @Override
    public List<TriggerDefinitionDTO> getTriggerDefinitions(final String jobName) {
        log.info("Getting schedulers for job(s)");

        final List<TriggerDefinitionDTO> result = new ArrayList<>();

        final List<? extends Trigger> triggers = getTriggers(jobName);

        for (final Trigger trigger : triggers) {
            final var state = scheduler.getTriggerState(trigger.getKey());
            result.add(QuartzUtil.mapToCronDefinition(trigger, state));
        }

        return result;
    }

    @SneakyThrows
    @Override
    @Transactional
    public void pauseTrigger(final String jobName) {
        log.info("Pause trigger {}", jobName);

        final TriggerKey key = findTriggerKey(jobName);
        final Trigger.TriggerState state = scheduler.getTriggerState(key);

        if (state == Trigger.TriggerState.PAUSED) {
            throw new ApiException("Trigger is already paused");
        }

        scheduler.pauseTrigger(key);
    }

    @SneakyThrows
    @Override
    @Transactional
    public void resumeTrigger(final String jobName) {
        log.info("Resume trigger {}", jobName);

        final TriggerKey key = findTriggerKey(jobName);
        final Trigger.TriggerState state = scheduler.getTriggerState(key);

        if (state == Trigger.TriggerState.NORMAL) {
            throw new ApiException("Trigger is working right now");
        }

        scheduler.resumeTrigger(key);
    }

    @SneakyThrows
    @Override
    @Transactional
    public void deleteTrigger(final String jobName) {
        log.info("Delete trigger {}", jobName);
        final TriggerKey key = findTriggerKey(jobName);
        if (!scheduler.unscheduleJob(key)) {
            throw new ApiException("Cannot delete trigger");
        }
    }

    @SneakyThrows
    private TriggerKey findTriggerKey(final String schedulerName) {
        return scheduler.getTriggerKeys(GroupMatcher.anyGroup())
                .stream()
                .filter(key -> key.getName().equalsIgnoreCase(schedulerName))
                .findFirst()
                .orElseThrow(() -> new ApiException("Scheduler is not found"));
    }

    @SneakyThrows
    private List<? extends Trigger> getTriggers(final String jobName) {
        if (jobName != null) {
            return scheduler.getTriggersOfJob(QuartzUtil.buildJobKey(jobName));
        }

        final Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(GroupMatcher.anyGroup());

        final List<Trigger> result = new ArrayList<>();

        for (final TriggerKey key : triggerKeys) {
            result.add(scheduler.getTrigger(key));
        }

        return result;
    }

    private Class<? extends Job> getJobTargetClass(final String jobName) {
        final Map<String, Job> jobs = context.getBeansOfType(Job.class);

        if (!jobs.containsKey(jobName)) {
            throw new ApiException("Cannot find job class");
        }

        final Job job = context.getBean(jobName, Job.class);
        final Class<?> targetClass = AopUtils.getTargetClass(job);

        @SuppressWarnings("unchecked")
        final Class<? extends Job> jobClass = (Class<? extends Job>) targetClass;

        return jobClass;
    }

    private JobDetail createNewJobDetail(final String jobName,
                                         final Class<? extends Job> jobClass) throws SchedulerException {

        final JobKey key = QuartzUtil.buildJobKey(jobName);

        log.info("Creating new job detail with key {}", key);

        final JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .withIdentity(key)
                .storeDurably()
                .build();

        // replacing existing jobs is set to false
        scheduler.addJob(jobDetail, false);

        return jobDetail;
    }

    private JobDetail findDetailByJobName(final String jobName) throws SchedulerException {
        final JobKey key = QuartzUtil.buildJobKey(jobName);
        return scheduler.getJobDetail(key);
    }
}
