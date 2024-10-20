package volunteer.plus.backend.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import volunteer.plus.backend.domain.dto.AddRequestResponseDTO;
import volunteer.plus.backend.service.AddRequestService;

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

    @GetMapping("/add-request/generate")
    @Operation(description = "Generate and retrieve verification codes for add request")
    public ResponseEntity<List<AddRequestResponseDTO>> generateCodes(@RequestParam final Long amount) {
        return ResponseEntity.ok(addRequestService.generate(amount));
    }


    @DeleteMapping("/add-request/delete")
    @Operation(description = "Delete all selected codes")
    public ResponseEntity<Void> deleteCodes(@RequestParam final Set<Long> ids) {
        addRequestService.deleteAll(ids);
        return ResponseEntity.ok().build();
    }
}
