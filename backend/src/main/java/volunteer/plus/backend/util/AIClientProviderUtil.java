package volunteer.plus.backend.util;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import volunteer.plus.backend.domain.enums.AIChatClient;

import java.util.Map;

import static volunteer.plus.backend.domain.enums.AIChatClient.*;
import static volunteer.plus.backend.domain.enums.AIChatClient.OLLAMA_MILITARY;

@Component
public class AIClientProviderUtil {
    private final Map<AIChatClient, ChatClient> openAIChatClientMap;
    private final Map<AIChatClient, ChatClient> ollamaChatClientMap;

    public AIClientProviderUtil(final @Qualifier("openAIChatClientMap") Map<AIChatClient, ChatClient> openAIChatClientMap,
                                final @Qualifier("ollamaChatClientMap") Map<AIChatClient, ChatClient> ollamaChatClientMap) {
        this.openAIChatClientMap = openAIChatClientMap;
        this.ollamaChatClientMap = ollamaChatClientMap;
    }

    public ChatClient getChatClient(final AIChatClient aiChatClient) {
        return switch (aiChatClient) {
            case OPENAI_DEFAULT -> openAIChatClientMap.get(OPENAI_DEFAULT);
            case OLLAMA_DEFAULT -> ollamaChatClientMap.get(OLLAMA_DEFAULT);

            case OPENAI_IN_MEMORY -> openAIChatClientMap.get(OPENAI_IN_MEMORY);
            case OLLAMA_IN_MEMORY -> ollamaChatClientMap.get(OLLAMA_IN_MEMORY);

            case OPENAI_MILITARY -> openAIChatClientMap.get(OPENAI_MILITARY);
            case OLLAMA_MILITARY -> ollamaChatClientMap.get(OLLAMA_MILITARY);
        };
    }
}
