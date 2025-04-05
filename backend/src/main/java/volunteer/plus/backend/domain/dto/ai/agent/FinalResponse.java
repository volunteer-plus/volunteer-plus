package volunteer.plus.backend.domain.dto.ai.agent;

import java.util.List;

public record FinalResponse(String analysis, List<String> workerResponses) {
}
