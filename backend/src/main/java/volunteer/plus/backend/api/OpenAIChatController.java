package volunteer.plus.backend.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import volunteer.plus.backend.service.ai.OpenAIService;

@Validated
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class OpenAIChatController {
    private final OpenAIService openAIService;

    @PostMapping("/open-ai/chat")
    @Operation(description = "Chat with OpenAI model chat")
    public ResponseEntity<ChatResponse> chat(@RequestBody final String message) {
        return ResponseEntity.ok(openAIService.chat(message));
    }

}
