package volunteer.plus.backend.service.ai.impl;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.moderation.ModerationResponse;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import volunteer.plus.backend.domain.dto.AIChatResponse;
import volunteer.plus.backend.service.ai.AIModerationService;
import volunteer.plus.backend.service.ai.OllamaAIService;

import java.util.concurrent.Future;


@Slf4j
@Service
public class OllamaAIServiceImpl implements OllamaAIService {
    private final ChatClient ollamaChatClient;
    private final AIModerationService moderationService;


    public OllamaAIServiceImpl(final @Qualifier("ollamaGeneralChatClient") ChatClient ollamaChatClient,
                               final AIModerationService moderationService) {
        this.ollamaChatClient = ollamaChatClient;
        this.moderationService = moderationService;
    }

    @Override
    @SneakyThrows
    public AIChatResponse chat(final String message) {
        log.info("Asking Ollama model: {}", message);

        // call async process of message moderation
        final Future<ModerationResponse> moderationFuture = moderationService.moderate(message);

        final Prompt prompt = new Prompt(message, OllamaOptions.builder().build());

        final ChatResponse chatResponse = ollamaChatClient.prompt(prompt)
                .call()
                .chatResponse();

        final ModerationResponse moderationResponse = moderationFuture.get();

        return AIChatResponse.builder()
                .chatResponse(chatResponse)
                .moderationResponse(moderationResponse)
                .build();
    }
}
