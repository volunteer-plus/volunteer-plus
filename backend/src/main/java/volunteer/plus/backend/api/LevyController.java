package volunteer.plus.backend.api;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import volunteer.plus.backend.domain.dto.LevyCreationRequestDTO;
import volunteer.plus.backend.domain.dto.LevyDTO;
import volunteer.plus.backend.service.general.LevyService;

import java.util.List;
import java.util.Set;


@Validated
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LevyController {
    private final LevyService levyService;

    @GetMapping("/levies")
    @Operation(description = "Retrieve levies")
    public ResponseEntity<Page<LevyDTO>> getLevies(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size,
                                                   @RequestParam(required = false) final Set<Long> requestIds) {
        return ResponseEntity.ok(levyService.getLevies(PageRequest.of(page, size), requestIds));
    }

    @PostMapping("/levies-create-or-update")
    @Operation(description = "Create levies with responsible volunteers")
    public ResponseEntity<List<LevyDTO>> createRequests(@RequestBody @Valid final LevyCreationRequestDTO levyCreationRequestDTO) {
        return ResponseEntity.ok(levyService.createOrUpdateLevies(levyCreationRequestDTO));
    }
}
