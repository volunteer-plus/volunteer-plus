package volunteer.plus.backend.domain.dto.ai.agent;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrchestratorWorkersDTO {
    @NotBlank
    private String message;
    @NotBlank
    private String orchestratorPrompt;
    @NotBlank
    private String workerPrompt;
}
