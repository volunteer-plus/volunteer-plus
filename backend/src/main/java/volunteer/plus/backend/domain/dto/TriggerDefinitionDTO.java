package volunteer.plus.backend.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.quartz.Trigger;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TriggerDefinitionDTO {
    @NotBlank
    private String name;

    @NotBlank
    private String cronExpression;

    private String taskName;

    private Trigger.TriggerState state;
}
