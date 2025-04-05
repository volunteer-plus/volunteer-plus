package volunteer.plus.backend.domain.dto.ai.agent;

import java.util.List;

public record OrchestratorResponse(String analysis, List<Task> tasks) {
}
