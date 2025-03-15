package volunteer.plus.backend.service.ai;

import org.springframework.ai.document.Document;
import org.springframework.web.multipart.MultipartFile;
import volunteer.plus.backend.domain.enums.AIProvider;

import java.util.List;

public interface VectorStoreAIService {
    List<Document> getData(AIProvider aiProvider, String query);
    void injectData(AIProvider aiProvider, MultipartFile multipartFile);
}
