package volunteer.plus.backend.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import volunteer.plus.backend.domain.dto.AIChatResponse;
import volunteer.plus.backend.domain.enums.AIChatClient;
import volunteer.plus.backend.service.ai.OllamaAIService;


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
}
