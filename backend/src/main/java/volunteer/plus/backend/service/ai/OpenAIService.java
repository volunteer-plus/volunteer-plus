package volunteer.plus.backend.service.ai;

import org.springframework.ai.chat.model.ChatResponse;
import volunteer.plus.backend.domain.dto.ImageGenerationRequestDTO;

import java.util.List;


public interface OpenAIService {
    ChatResponse chat(String message);

    List<String> generateImage(ImageGenerationRequestDTO imageGenerationRequestDTO);
}
