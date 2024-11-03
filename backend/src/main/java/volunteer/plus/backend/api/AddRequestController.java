package volunteer.plus.backend.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import volunteer.plus.backend.domain.dto.AddRequestResponseDTO;
import volunteer.plus.backend.service.general.AddRequestService;

import java.util.List;
import java.util.Set;

@Validated
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AddRequestController {
    private final AddRequestService addRequestService;

    @GetMapping("/add-requests")
    @Operation(description = "Retrieve add requests")
    public ResponseEntity<List<AddRequestResponseDTO>> getRequests() {
        return ResponseEntity.ok(addRequestService.getRequests());
    }

    @PostMapping("/add-request/generate")
    @Operation(description = "Generate and retrieve verification codes for add request for specific regiment")
    public ResponseEntity<List<AddRequestResponseDTO>> generateCodes(@RequestParam final String regimentCode,
                                                                     @RequestParam final Long amount) {
        return ResponseEntity.ok(addRequestService.generate(regimentCode, amount));
    }

    @PostMapping("/add-request/validate")
    @Operation(description = "Validate code")
    public ResponseEntity<Void> validateCode(@RequestParam final String code) {
        addRequestService.validateCode(code);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/add-request/delete")
    @Operation(description = "Delete all selected codes")
    public ResponseEntity<Void> deleteCodes(@RequestParam final Set<Long> ids) {
        addRequestService.deleteAll(ids);
        return ResponseEntity.ok().build();
    }
}
