package volunteer.plus.backend.service.ai;

import volunteer.plus.backend.domain.dto.AIChatResponse;
import volunteer.plus.backend.domain.enums.AIChatClient;

public interface OllamaAIService {
    AIChatResponse chat(AIChatClient aiChatClient, String message);
}
