package volunteer.plus.backend.service.ai.impl;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.moderation.ModerationResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import volunteer.plus.backend.domain.dto.AIChatResponse;
import volunteer.plus.backend.domain.enums.AIChatClient;
import volunteer.plus.backend.service.ai.AIModerationService;
import volunteer.plus.backend.service.ai.OllamaAIService;
import volunteer.plus.backend.service.websocket.WebSocketService;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import static volunteer.plus.backend.config.websocket.WebSocketConfig.OLLAMA_CHAT_CLIENT_TARGET;
import static volunteer.plus.backend.util.AIUtil.getAIMediaList;


@Slf4j
@Service
public class OllamaAIServiceImpl implements OllamaAIService {
    private final Map<AIChatClient, ChatClient> ollamaChatClientMap;
    private final AIModerationService moderationService;
    private final WebSocketService webSocketService;


    public OllamaAIServiceImpl(final @Qualifier("ollamaChatClientMap") Map<AIChatClient, ChatClient> ollamaChatClientMap,
                               final AIModerationService moderationService,
                               final WebSocketService webSocketService) {
        this.ollamaChatClientMap = ollamaChatClientMap;
        this.moderationService = moderationService;
        this.webSocketService = webSocketService;
    }

    @Override
    @SneakyThrows
    public AIChatResponse chat(final AIChatClient aiChatClient,
                               final String message,
                               final List<MultipartFile> multipartFiles) {
        log.info("Asking Ollama model: {}", message);

        // call async process of message moderation
        final Future<ModerationResponse> moderationFuture = moderationService.moderate(message);

        final UserMessage um = new UserMessage(
                message,
                getAIMediaList(multipartFiles)
        );

        final ChatClient chatClient = ollamaChatClientMap.get(aiChatClient);
        final String chatResponse = chatClient
                .prompt(new Prompt(um))
                .call()
                .content();

        final ModerationResponse moderationResponse = moderationFuture.get();

        webSocketService.sendNotification(OLLAMA_CHAT_CLIENT_TARGET, "Ollama request:\n" + message + "\nResponse:\n" + chatResponse);

        return AIChatResponse.builder()
                .chatResponse(chatResponse)
                .moderationResponse(moderationResponse)
                .build();
    }
}
