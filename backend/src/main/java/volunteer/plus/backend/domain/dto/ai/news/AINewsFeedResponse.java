package volunteer.plus.backend.domain.dto.ai.news;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public final class AINewsFeedResponse {
    private String subject;
    private String body;
}
