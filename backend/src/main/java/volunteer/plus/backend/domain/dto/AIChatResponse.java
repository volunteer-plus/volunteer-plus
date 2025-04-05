package volunteer.plus.backend.domain.dto;

import lombok.*;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.ai.moderation.ModerationResponse;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AIChatResponse {
    private String chatResponse;
    private ModerationResponse moderationResponse;
    private EvaluationResponse relevancyResponse;
    private EvaluationResponse factCheckingResponse;
}
