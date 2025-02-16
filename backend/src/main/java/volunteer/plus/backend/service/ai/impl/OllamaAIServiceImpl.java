package volunteer.plus.backend.service.ai.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import volunteer.plus.backend.service.ai.OllamaAIService;


@Slf4j
@Service
public class OllamaAIServiceImpl implements OllamaAIService {
    private final ChatClient ollamaChatClient;

    public OllamaAIServiceImpl(final @Qualifier("ollamaGeneralChatClient") ChatClient ollamaChatClient) {
        this.ollamaChatClient = ollamaChatClient;
    }

    @Override
    public ChatResponse chat(final String message) {
        log.info("Asking Ollama model: {}", message);

        final Prompt prompt = new Prompt(message, OllamaOptions.builder().build());

        return ollamaChatClient.prompt(prompt)
                .call()
                .chatResponse();
    }
}
