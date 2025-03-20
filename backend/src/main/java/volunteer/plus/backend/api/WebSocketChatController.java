package volunteer.plus.backend.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import volunteer.plus.backend.domain.enums.AIChatClient;
import volunteer.plus.backend.service.ai.OllamaAIService;
import volunteer.plus.backend.service.ai.OpenAIService;

import java.util.List;

import static volunteer.plus.backend.config.websocket.WebSocketConfig.*;
import static volunteer.plus.backend.config.websocket.WebSocketConfig.OPENAI_RESPONSE_TARGET;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WebSocketChatController {
    private final OllamaAIService ollamaAIService;
    private final OpenAIService openAIService;

    @MessageMapping(OLLAMA_MESSAGE_MAPPING)
    @SendTo(OLLAMA_RESPONSE_TARGET)
    public String chatOllamaAI(@Payload final String message) {
        return ollamaAIService.chat(AIChatClient.DEFAULT, message, List.of()).getChatResponse();
    }


    @MessageMapping(OPENAI_MESSAGE_MAPPING)
    @SendTo(OPENAI_RESPONSE_TARGET)
    public String chatOpenAI(@Payload final String message) {
        return openAIService.chat(AIChatClient.DEFAULT, message, List.of()).getChatResponse();
    }
}
