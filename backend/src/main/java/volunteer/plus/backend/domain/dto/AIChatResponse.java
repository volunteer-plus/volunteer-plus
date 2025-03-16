package volunteer.plus.backend.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.moderation.ModerationResponse;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AIChatResponse {
    private String chatResponse;
    private ModerationResponse moderationResponse;
}
