package volunteer.plus.backend.domain.dto.ai.agent;

import java.util.List;

public record RefinedResponse(String solution, List<Generation> chainOfThought) {
}
