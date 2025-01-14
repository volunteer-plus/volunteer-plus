package volunteer.plus.backend.service.ai;

import org.springframework.ai.chat.model.ChatResponse;

public interface OpenAIService {
    ChatResponse chat(String message);
}
