package volunteer.plus.backend.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import volunteer.plus.backend.domain.enums.AIProvider;
import volunteer.plus.backend.service.ai.VectorStoreAIService;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/ai/vector-store")
@RequiredArgsConstructor
@ConditionalOnProperty(value = "spring.redis.disabled", havingValue = "false")
public class VectorStoreAIController {
    private final VectorStoreAIService vectorStoreAIService;

    @GetMapping("/list-data")
    @Operation(description = "Get data from vector store")
    public ResponseEntity<List<Document>> getData(@RequestParam final AIProvider aiProvider,
                                                  @RequestParam(defaultValue = "100") final int topK,
                                                  @RequestBody(required = false) final String query) {
        return ResponseEntity.ok(vectorStoreAIService.getData(aiProvider, topK, query));
    }

    @PostMapping("/inject-documents")
    @Operation(description = "Inject file data to vector store")
    public ResponseEntity<Void> injectData(@RequestParam final AIProvider aiProvider,
                                           @RequestBody final MultipartFile file) {
        vectorStoreAIService.injectData(aiProvider, file);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete-data")
    @Operation(description = "Delete data from vector store")
    public ResponseEntity<Void> deleteData(@RequestParam final AIProvider aiProvider,
                                           @RequestParam final List<String> ids) {
        vectorStoreAIService.deleteData(aiProvider, ids);
        return ResponseEntity.ok().build();
    }
}
