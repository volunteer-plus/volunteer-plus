package volunteer.plus.backend.service.ai;

import org.springframework.ai.image.ImageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import volunteer.plus.backend.domain.dto.AIChatResponse;
import volunteer.plus.backend.domain.dto.ImageGenerationRequestDTO;
import volunteer.plus.backend.domain.enums.AIChatClient;

import java.util.List;
import java.util.concurrent.Future;


public interface OpenAIService {
    AIChatResponse chat(AIChatClient aiChatClient, String message);

    ResponseEntity<byte[]> generateImage(ImageGenerationRequestDTO imageGenerationRequestDTO);

    List<String> generateImageUrls(ImageGenerationRequestDTO imageGenerationRequestDTO);

    String generateTextFromAudio(String lang, MultipartFile file);

    ResponseEntity<byte[]> generateAudioFromText(final String message);

    Future<ImageResponse> getImageResponse(ImageGenerationRequestDTO imageGenerationRequestDTO, int n);
}
