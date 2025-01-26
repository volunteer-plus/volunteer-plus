package volunteer.plus.backend.service.ai;

import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import volunteer.plus.backend.domain.dto.ImageGenerationRequestDTO;

import java.util.List;


public interface OpenAIService {
    ChatResponse chat(String message);

    Flux<String> streamingChat(String message);

    List<String> generateImage(ImageGenerationRequestDTO imageGenerationRequestDTO);

    String generateTextFromAudio(String lang, MultipartFile file);

    ResponseEntity<byte[]> generateAudioFromText(final String message);
}
