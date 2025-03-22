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
import volunteer.plus.backend.domain.dto.TaskDTO;
import volunteer.plus.backend.domain.dto.TaskDefinitionDTO;
import volunteer.plus.backend.domain.dto.TriggerDefinitionDTO;
import volunteer.plus.backend.exceptions.ApiException;
import volunteer.plus.backend.service.quartz.TaskService;
import volunteer.plus.backend.util.QuartzUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final ApplicationContext context;
    private final Scheduler scheduler;

    @Override
    public List<TaskDTO> findAvailableTasks() {
        log.info("Getting all quartz jobs...");
        final Map<String, Job> jobs = context.getBeansOfType(Job.class);
        return jobs.keySet()
                .stream()
                .map(job ->
                        TaskDTO.builder()
                                .name(job)
                                .schedulers(getTriggerDefinitions(job))
                                .build()
                )
                .toList();
    }

    @SneakyThrows
    @Override
    public void launchTask(final TaskDTO taskDTO) {
        log.info("Launching task {}", taskDTO);

        final Class<? extends Job> jobClass = getJobTargetClass(taskDTO.getName());

        JobDetail detail = findDetailByTaskName(taskDTO.getName());

        if (detail == null) {
            detail = createNewJobDetail(taskDTO.getName(), jobClass);
        }

        scheduler.triggerJob(detail.getKey());
    }

    @SneakyThrows
    @Override
    @Transactional
    public void scheduleTask(final TaskDefinitionDTO taskDefinitionDTO) {
        log.info("Scheduling task {}", taskDefinitionDTO);

        final Class<? extends Job> jobClass = getJobTargetClass(taskDefinitionDTO.getTaskName());

        JobDetail detail = findDetailByTaskName(taskDefinitionDTO.getTaskName());

        if (detail == null) {
            detail = createNewJobDetail(taskDefinitionDTO.getTaskName(), jobClass);
        }

        final CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                .withIdentity(taskDefinitionDTO.getTrigger().getName(), Key.DEFAULT_GROUP)
                .withSchedule(CronScheduleBuilder.cronSchedule(taskDefinitionDTO.getTrigger().getCronExpression()))
                .forJob(detail.getKey())
                .build();

        if (scheduler.checkExists(cronTrigger.getKey())) {
            throw new ApiException("Cron key already exists");
        }

        for (final Trigger trigger : scheduler.getTriggersOfJob(detail.getKey())) {
            if (trigger instanceof CronTrigger cron && taskDefinitionDTO.getTrigger().getCronExpression().equalsIgnoreCase(cron.getCronExpression())) {
                throw new ApiException("Trigger with cron " + cron.getCronExpression() + " already exists");
            }
        }

        scheduler.scheduleJob(cronTrigger);
    }

    @SneakyThrows
    @Override
    public List<TriggerDefinitionDTO> getTriggerDefinitions(final String taskName) {
        log.info("Getting schedulers for task(s)");

        final List<TriggerDefinitionDTO> result = new ArrayList<>();

        final List<? extends Trigger> triggers = getTriggers(taskName);

        for (final Trigger trigger : triggers) {
            final var state = scheduler.getTriggerState(trigger.getKey());
            result.add(QuartzUtil.mapToCronDefinition(trigger, state));
        }

        return result;
    }

    @SneakyThrows
    @Override
    @Transactional
    public void pauseTrigger(final String triggerName) {
        log.info("Pause trigger {}", triggerName);

        final TriggerKey key = findTriggerKey(triggerName);
        final Trigger.TriggerState state = scheduler.getTriggerState(key);

        if (state == Trigger.TriggerState.PAUSED) {
            throw new ApiException("Trigger is already paused");
        }

        scheduler.pauseTrigger(key);
    }

    @SneakyThrows
    @Override
    @Transactional
    public void resumeTrigger(final String triggerName) {
        log.info("Resume trigger {}", triggerName);

        final TriggerKey key = findTriggerKey(triggerName);
        final Trigger.TriggerState state = scheduler.getTriggerState(key);

        if (state == Trigger.TriggerState.NORMAL) {
            throw new ApiException("Trigger is working right now");
        }

        scheduler.resumeTrigger(key);
    }

    @SneakyThrows
    @Override
    @Transactional
    public void deleteTrigger(final String triggerName) {
        log.info("Delete trigger {}", triggerName);
        final TriggerKey key = findTriggerKey(triggerName);
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
    private List<? extends Trigger> getTriggers(final String taskName) {
        if (taskName != null) {
            return scheduler.getTriggersOfJob(QuartzUtil.buildJobKey(taskName));
        }

        final Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(GroupMatcher.anyGroup());

        final List<Trigger> result = new ArrayList<>();

        for (final TriggerKey key : triggerKeys) {
            result.add(scheduler.getTrigger(key));
        }

        return result;
    }

    private Class<? extends Job> getJobTargetClass(final String taskName) {
        final Map<String, Job> jobs = context.getBeansOfType(Job.class);

        if (!jobs.containsKey(taskName)) {
            throw new ApiException("Cannot find job class");
        }

        final Job job = context.getBean(taskName, Job.class);
        final Class<?> taskClass = AopUtils.getTargetClass(job);

        @SuppressWarnings("unchecked")
        final Class<? extends Job> jobClass = (Class<? extends Job>) taskClass;

        return jobClass;
    }

    private JobDetail createNewJobDetail(final String taskName,
                                         final Class<? extends Job> jobClass) throws SchedulerException {

        final JobKey key = QuartzUtil.buildJobKey(taskName);

        log.info("Creating new job detail with key {}", key);

        final JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .withIdentity(key)
                .storeDurably()
                .build();

        // replacing existing jobs is set to false
        scheduler.addJob(jobDetail, false);

        return jobDetail;
    }

    private JobDetail findDetailByTaskName(final String taskName) throws SchedulerException {
        final JobKey key = QuartzUtil.buildJobKey(taskName);
        return scheduler.getJobDetail(key);
    }
}
