package volunteer.plus.backend.service.ai;

import org.springframework.web.multipart.MultipartFile;
import volunteer.plus.backend.domain.dto.AIChatResponse;
import volunteer.plus.backend.domain.enums.AIChatClient;

import java.util.List;

public interface OllamaAIService {
    AIChatResponse chat(AIChatClient aiChatClient, String message, List<MultipartFile> multipartFiles);
}
