package volunteer.plus.backend.service.ai;

import org.springframework.web.multipart.MultipartFile;
import volunteer.plus.backend.domain.dto.AIChatResponse;
import volunteer.plus.backend.domain.enums.AIChatClient;
import volunteer.plus.backend.domain.enums.OllamaAIModel;

import java.util.List;

public interface OllamaAIService {
    AIChatResponse chat(AIChatClient aiChatClient, OllamaAIModel aiLlamaModel, String message, List<MultipartFile> multipartFiles);
}
