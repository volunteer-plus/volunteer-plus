package volunteer.plus.backend.service.ai;

import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.image.ImageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import volunteer.plus.backend.domain.dto.ImageGenerationRequestDTO;
import volunteer.plus.backend.domain.enums.OpenAIClient;

import java.util.List;
import java.util.concurrent.Future;


public interface OpenAIService {
    ChatResponse chat(OpenAIClient openAIClient, String message);

    Flux<String> streamingChat(OpenAIClient openAIClient, String message);

    ResponseEntity<byte[]> generateImage(ImageGenerationRequestDTO imageGenerationRequestDTO);

    List<String> generateImageUrls(ImageGenerationRequestDTO imageGenerationRequestDTO);

    String generateTextFromAudio(String lang, MultipartFile file);

    ResponseEntity<byte[]> generateAudioFromText(final String message);

    Future<ImageResponse> getImageResponse(ImageGenerationRequestDTO imageGenerationRequestDTO, int n);
}
