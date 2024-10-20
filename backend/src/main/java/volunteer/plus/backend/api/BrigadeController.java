package volunteer.plus.backend.api;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import volunteer.plus.backend.dto.BrigadeCreationRequestDTO;
import volunteer.plus.backend.dto.BrigadeDTO;
import volunteer.plus.backend.service.BrigadeCodesService;
import volunteer.plus.backend.service.BrigadeService;

import java.util.List;
import java.util.Set;

@Validated
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BrigadeController {
    private final BrigadeCodesService brigadeCodesService;
    private final BrigadeService brigadeService;

    @GetMapping("/brigades-codes")
    @Operation(description = "Retrieve all valid codes in system of military units for further validation")
    public ResponseEntity<List<String>> getCodes() {
        return ResponseEntity.ok(brigadeCodesService.getCodes());
    }

    @GetMapping("/brigades")
    @Operation(description = "Retrieve all brigades entities")
    public ResponseEntity<List<BrigadeDTO>> getBrigades(@RequestParam(required = false) final Set<Long> ids) {
        return ResponseEntity.ok(brigadeService.getBrigades(ids));
    }

    @PostMapping("/brigade/create-or-update")
    @Operation(description = "Create or update brigades")
    public ResponseEntity<List<BrigadeDTO>> createOrUpdate(@RequestBody @Valid BrigadeCreationRequestDTO creationRequestDTO) {
        return ResponseEntity.ok(brigadeService.createOrUpdate(creationRequestDTO));
    }

    @DeleteMapping("/brigade/delete")
    @Operation(description = "Delete brigades")
    public ResponseEntity<Void> delete(@RequestParam final Set<Long> ids) {
        brigadeService.deleteAll(ids);
        return ResponseEntity.ok().build();
    }
}
