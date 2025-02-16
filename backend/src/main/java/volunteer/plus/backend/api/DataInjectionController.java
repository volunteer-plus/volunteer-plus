package volunteer.plus.backend.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import volunteer.plus.backend.service.ai.DataInjectionService;

@Validated
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class DataInjectionController {
    private final DataInjectionService dataInjectionService;

    @PostMapping("/inject/vector-documents")
    @Operation(description = "Inject file data to vector stores")
    public ResponseEntity<Void> injectData(@RequestBody final MultipartFile file) {
        dataInjectionService.injectData(file);
        return ResponseEntity.ok().build();
    }
}
