package volunteer.plus.backend.service.ai.impl;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.FactCheckingEvaluator;
import org.springframework.ai.evaluation.RelevancyEvaluator;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import volunteer.plus.backend.domain.dto.AIChatResponse;
import volunteer.plus.backend.domain.enums.AIChatClient;
import volunteer.plus.backend.domain.enums.OllamaAIModel;
import volunteer.plus.backend.exceptions.ApiException;
import volunteer.plus.backend.exceptions.ErrorCode;
import volunteer.plus.backend.service.ai.AIModerationService;
import volunteer.plus.backend.service.ai.OllamaAIService;
import volunteer.plus.backend.service.ai.tools.AIAgentPattern;
import volunteer.plus.backend.service.ai.tools.AIMilitaryTools;
import volunteer.plus.backend.service.websocket.WebSocketService;

import java.util.List;
import java.util.Map;

import static volunteer.plus.backend.config.websocket.WebSocketConfig.OLLAMA_CHAT_CLIENT_TARGET;
import static volunteer.plus.backend.util.AIUtil.getAIMediaList;


@Slf4j
@Service
public class OllamaAIServiceImpl implements OllamaAIService {
    private final Map<AIChatClient, ChatClient> ollamaChatClientMap;
    private final WebSocketService webSocketService;
    private final AIMilitaryTools aiMilitaryTools;
    private final List<AIAgentPattern> aiAgentPatterns;
    private final AIModerationService moderationService;
    private final ChatModel chatModel;


    public OllamaAIServiceImpl(final @Qualifier("ollamaChatClientMap") Map<AIChatClient, ChatClient> ollamaChatClientMap,
                               final WebSocketService webSocketService,
                               final AIMilitaryTools aiMilitaryTools,
                               final List<AIAgentPattern> aiAgentPatterns,
                               final AIModerationService moderationService,
                               final @Qualifier("openAiChatModel") ChatModel chatModel) {
        this.ollamaChatClientMap = ollamaChatClientMap;
        this.webSocketService = webSocketService;
        this.aiMilitaryTools = aiMilitaryTools;
        this.aiAgentPatterns = aiAgentPatterns;
        this.moderationService = moderationService;
        this.chatModel = chatModel;
    }

    @Override
    @SneakyThrows
    public AIChatResponse chat(final AIChatClient aiChatClient,
                               final OllamaAIModel ollamaModel,
                               final String message,
                               final List<MultipartFile> multipartFiles) {
        log.info("Asking Ollama model: {}", message);

        final var moderationFuture = moderationService.moderate(message);

        final UserMessage um = new UserMessage(
                message,
                getAIMediaList(multipartFiles)
        );

        final ChatClient chatClient = ollamaChatClientMap.get(aiChatClient);

        if (chatClient == null) {
            throw new ApiException(ErrorCode.CHAT_CLIENT_NOT_FOUND);
        }

        final RelevancyEvaluator relevancyEvaluator = new RelevancyEvaluator(ChatClient.builder(chatModel));
        final FactCheckingEvaluator factCheckingEvaluator = new FactCheckingEvaluator(ChatClient.builder(chatModel));

        final String chatResponse = getChatResponse(chatClient, um, ollamaModel);

        final EvaluationRequest evaluationRequest = new EvaluationRequest(message, chatResponse);

        webSocketService.sendNotification(OLLAMA_CHAT_CLIENT_TARGET, "Ollama request:\n" + message + "\nResponse:\n" + chatResponse);

        return AIChatResponse.builder()
                .chatResponse(chatResponse)
                .moderationResponse(moderationFuture.get())
                .relevancyResponse(relevancyEvaluator.evaluate(evaluationRequest))
                .factCheckingResponse(factCheckingEvaluator.evaluate(evaluationRequest))
                .build();
    }

    private String getChatResponse(final ChatClient chatClient,
                                   final UserMessage um,
                                   final OllamaAIModel ollamaModel) {
        if (ollamaModel == OllamaAIModel.LLAMA) {
            return chatClient
                    .prompt(new Prompt(um))
                    .options(
                            OllamaOptions.builder()
                                    .model(ollamaModel.getModelName())
                                    .build()
                    )
                    .tools(aiMilitaryTools, aiAgentPatterns)
                    .call()
                    .content();
        }
        return chatClient
                .prompt(new Prompt(um))
                .options(
                        OllamaOptions.builder()
                                .model(ollamaModel.getModelName())
                                .build()
                )
                .call()
                .content();
    }
}
