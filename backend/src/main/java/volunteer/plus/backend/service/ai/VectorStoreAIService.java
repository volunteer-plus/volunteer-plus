package volunteer.plus.backend.service.ai;

import org.springframework.ai.document.Document;
import org.springframework.web.multipart.MultipartFile;
import volunteer.plus.backend.domain.enums.AIProvider;

import java.time.LocalDate;
import java.util.List;

public interface VectorStoreAIService {
    List<Document> getData(AIProvider aiProvider, int topK, String query);

    void injectData(AIProvider aiProvider, MultipartFile multipartFile);

    void injectWarStatisticsData(AIProvider aiProvider, LocalDate dateFrom, LocalDate dateTo);

    void deleteData(AIProvider aiProvider, List<String> ids);
}
