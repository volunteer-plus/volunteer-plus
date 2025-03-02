package volunteer.plus.backend.service.ai;

import volunteer.plus.backend.domain.dto.AIChatResponse;

public interface OllamaAIService {
    AIChatResponse chat(String message);
}
