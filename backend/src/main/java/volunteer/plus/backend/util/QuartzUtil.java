package volunteer.plus.backend.util;

import org.quartz.CronTrigger;
import org.quartz.JobKey;
import org.quartz.Trigger;
import org.quartz.utils.Key;
import volunteer.plus.backend.domain.dto.TriggerDefinitionDTO;

public class QuartzUtil {
    private QuartzUtil() {
    }

    public static TriggerDefinitionDTO mapToCronDefinition(final Trigger trigger,
                                                           final Trigger.TriggerState state) {
        final TriggerDefinitionDTO def = new TriggerDefinitionDTO();

        def.setName(trigger.getKey().getName());
        def.setState(state);

        if (trigger instanceof CronTrigger cronTrigger) {
            def.setCronExpression(cronTrigger.getCronExpression());
        }

        if (trigger.getJobKey() != null) {
            def.setJobName(trigger.getJobKey().getName());
        }

        return def;
    }

    public static JobKey buildJobKey(final String taskName) {
        return JobKey.jobKey(taskName, Key.DEFAULT_GROUP);
    }
}
