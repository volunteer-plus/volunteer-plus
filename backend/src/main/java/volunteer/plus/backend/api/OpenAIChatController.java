package volunteer.plus.backend.api;

import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<String> chat(@RequestBody String message) {
        return ResponseEntity.ok(openAIService.chat(message));
    }

}
