package volunteer.plus.backend.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuartzJobDTO {
    @NotBlank
    private String name;

    private List<TriggerDefinitionDTO> triggers = new ArrayList<>();
}
