package volunteer.plus.backend.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum AIChatClient {
    OPENAI_DEFAULT(ChatEmail.OPEN_AI_USER_EMAIL),
    OLLAMA_DEFAULT(ChatEmail.OLLAMA_USER_EMAIL),

    OPENAI_IN_MEMORY(ChatEmail.OPEN_AI_USER_EMAIL),
    OLLAMA_IN_MEMORY(ChatEmail.OLLAMA_USER_EMAIL),

    OPENAI_MILITARY(ChatEmail.OPEN_AI_USER_EMAIL),
    OLLAMA_MILITARY(ChatEmail.OLLAMA_USER_EMAIL);

    private final String userEmail;

    public static class ChatEmail {
        private ChatEmail() {
        }

        public static final String OLLAMA_USER_EMAIL = "ollamaai@test.com";
        public static final String OPEN_AI_USER_EMAIL = "openai@test.com";
    }
}
