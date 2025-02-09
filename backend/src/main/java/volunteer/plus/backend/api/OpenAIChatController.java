package volunteer.plus.backend.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import volunteer.plus.backend.domain.dto.ImageGenerationRequestDTO;
import volunteer.plus.backend.domain.enums.OpenAIClient;
import volunteer.plus.backend.service.ai.DataInjectionService;
import volunteer.plus.backend.service.ai.OpenAIService;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class OpenAIChatController {
    private final OpenAIService openAIService;
    private final DataInjectionService dataInjectionService;

    @PostMapping("/open-ai/generate/chat")
    @Operation(description = "Chat with OpenAI model chat")
    public ResponseEntity<ChatResponse> chat(@RequestParam final OpenAIClient openAIClient,
                                             @RequestBody final String message) {
        return ResponseEntity.ok(openAIService.chat(openAIClient, message));
    }

    @PostMapping("/open-ai/generate/streaming/chat")
    @Operation(description = "Streaming Chat with OpenAI model chat")
    public ResponseEntity<Flux<String>> streamingChat(@RequestParam final OpenAIClient openAIClient,
                                                      @RequestBody final String message) {
        return ResponseEntity.ok(openAIService.streamingChat(openAIClient, message));
    }

    @PostMapping("/open-ai/generate/image")
    @Operation(description = "Image generation with OpenAI model")
    public ResponseEntity<byte[]> generateImage(@RequestBody final ImageGenerationRequestDTO imageGenerationRequestDTO) {
        return openAIService.generateImage(imageGenerationRequestDTO);
    }

    @PostMapping("/open-ai/generate/image-url")
    @Operation(description = "Image generation with OpenAI model")
    public ResponseEntity<List<String>> generateImageUrls(@RequestBody final ImageGenerationRequestDTO imageGenerationRequestDTO) {
        return ResponseEntity.ok(openAIService.generateImageUrls(imageGenerationRequestDTO));
    }

    @PostMapping("/open-ai/generate/text-from-audio")
    @Operation(description = "Text transcription generation from audio")
    public ResponseEntity<String> generateTextFromAudio(@RequestParam final String lang,
                                                        @RequestBody final MultipartFile file) {
        return ResponseEntity.ok(openAIService.generateTextFromAudio(lang, file));
    }

    @PostMapping("/open-ai/generate/audio-from-text")
    @Operation(description = "Audio generation from text")
    public ResponseEntity<byte[]> generateAudioFromText(@RequestBody final String message) {
        return openAIService.generateAudioFromText(message);
    }

    @PostMapping("/open-ai/inject/vector-documents")
    @Operation(description = "Inject file data to vector store")
    public ResponseEntity<Void> injectData(@RequestBody final MultipartFile file) {
        dataInjectionService.injectData(file);
        return ResponseEntity.ok().build();
    }
}
