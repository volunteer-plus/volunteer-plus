package volunteer.plus.backend.service.ai;

import org.springframework.ai.chat.model.ChatResponse;

public interface OllamaAIService {
    ChatResponse chat(String message);
}
