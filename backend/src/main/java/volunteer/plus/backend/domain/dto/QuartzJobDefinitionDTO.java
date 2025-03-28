package volunteer.plus.backend.domain.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuartzJobDefinitionDTO {
    @NotBlank
    private String jobName;

    @NotNull
    @Valid
    private TriggerDefinitionDTO trigger;
}
