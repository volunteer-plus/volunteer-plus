package volunteer.plus.backend.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static volunteer.plus.backend.service.websocket.impl.WSChatServiceImpl.*;

@Getter
@RequiredArgsConstructor
public enum AIChatClient {
    OPENAI_DEFAULT(OPEN_AI_USER_EMAIL),
    OLLAMA_DEFAULT(OLLAMA_USER_EMAIL),

    OPENAI_IN_MEMORY(OPEN_AI_USER_EMAIL),
    OLLAMA_IN_MEMORY(OLLAMA_USER_EMAIL),

    OPENAI_MILITARY(OPEN_AI_USER_EMAIL),
    OLLAMA_MILITARY(OLLAMA_USER_EMAIL);

    private final String userEmail;
}
