package volunteer.plus.backend.domain.dto.ai.agent;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParallelizationWorkflowRequestDTO {
    @NotBlank
    private String message;
    @NotNull
    private List<String> inputs;
    @NotNull
    private int nWorkers;
}
