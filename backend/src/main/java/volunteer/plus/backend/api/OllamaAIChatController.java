package volunteer.plus.backend.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import volunteer.plus.backend.domain.dto.AIChatResponse;
import volunteer.plus.backend.domain.enums.AIChatClient;
import volunteer.plus.backend.service.ai.OllamaAIService;

import static volunteer.plus.backend.config.websocket.WebSocketConfig.OLLAMA_MESSAGE_MAPPING;
import static volunteer.plus.backend.config.websocket.WebSocketConfig.OLLAMA_RESPONSE_TARGET;

@Validated
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class OllamaAIChatController {
    private final OllamaAIService ollamaAIService;

    @PostMapping("/ollama/generate/chat")
    @Operation(description = "Chat with Ollama model chat")
    public ResponseEntity<AIChatResponse> chat(@RequestParam final AIChatClient aiChatClient,
                                               @RequestBody final String message) {
        return ResponseEntity.ok(ollamaAIService.chat(aiChatClient, message));
    }

    @MessageMapping(OLLAMA_MESSAGE_MAPPING)
    @SendTo(OLLAMA_RESPONSE_TARGET)
    public String chat(@Payload final String message) {
       return ollamaAIService.chat(AIChatClient.DEFAULT, message)
                .getChatResponse()
                .getResult()
                .getOutput()
                .getContent();
    }
}
