package volunteer.plus.backend.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import volunteer.plus.backend.domain.dto.AIChatResponse;
import volunteer.plus.backend.domain.enums.AIChatClient;
import volunteer.plus.backend.domain.enums.OllamaAIModel;
import volunteer.plus.backend.service.ai.OllamaAIService;

import java.util.List;


@Validated
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class OllamaAIChatController {
    private final OllamaAIService ollamaAIService;

    @PostMapping("/ollama/generate/chat")
    @Operation(description = "Chat with Ollama model chat")
    public ResponseEntity<AIChatResponse> chat(@RequestParam final AIChatClient aiChatClient,
                                               @RequestParam final OllamaAIModel ollamaModel,
                                               @RequestPart("message") final String message,
                                               @RequestPart(name = "file", required = false) List<MultipartFile> multipartFiles) {
        return ResponseEntity.ok(ollamaAIService.chat(aiChatClient, ollamaModel, message, multipartFiles));
    }
}
