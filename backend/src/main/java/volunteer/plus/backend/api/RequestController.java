package volunteer.plus.backend.api;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import volunteer.plus.backend.domain.dto.RequestCreationRequestDTO;
import volunteer.plus.backend.domain.dto.RequestDTO;
import volunteer.plus.backend.service.RequestService;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;

    @GetMapping("/requests")
    @Operation(description = "Retrieve all requests")
    public ResponseEntity<Page<RequestDTO>> getRequests(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(requestService.getRequests(PageRequest.of(page, size)));
    }

    @PostMapping("/request-create")
    @Operation(description = "Create or update requests with creators")
    public ResponseEntity<List<RequestDTO>> createRequests(@RequestParam(required = false) final String userEmail,
                                                           @RequestParam(required = false) final Long militaryPersonnelId,
                                                           @RequestBody @Valid final RequestCreationRequestDTO requestCreationRequestDTO) {
        return ResponseEntity.ok(requestService.createOrUpdateRequests(userEmail, militaryPersonnelId, requestCreationRequestDTO));
    }
}
