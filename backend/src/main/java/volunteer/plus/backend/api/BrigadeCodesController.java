package volunteer.plus.backend.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import volunteer.plus.backend.service.BrigadeCodesService;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BrigadeCodesController {
    private final BrigadeCodesService brigadeCodesService;

    @GetMapping("/brigade/codes")
    @Operation(description = "Retrieve all valid codes in system of military units for further validation")
    public ResponseEntity<List<String>> getCodes() {
        return ResponseEntity.ok(brigadeCodesService.getCodes());
    }
}
