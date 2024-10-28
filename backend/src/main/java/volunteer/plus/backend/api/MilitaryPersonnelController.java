package volunteer.plus.backend.api;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import volunteer.plus.backend.domain.dto.BrigadeDTO;
import volunteer.plus.backend.domain.dto.MilitaryPersonnelCreationRequestDTO;
import volunteer.plus.backend.service.general.MilitaryPersonnelService;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Validated
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MilitaryPersonnelController {
    private final MilitaryPersonnelService militaryPersonnelService;

    @GetMapping("/military-personnel")
    @Operation(description = "Retrieve all military personnel of specified or all brigades")
    public ResponseEntity<Map<String, List<BrigadeDTO.MilitaryPersonnelDTO>>> getMilitaryPersonnel(@RequestParam(required = false) final Set<String> regimentCodes) {
        return ResponseEntity.ok(militaryPersonnelService.getMilitaryPersonnel(regimentCodes));
    }

    @PostMapping("/military-personnel/create")
    @Operation(description = "Create a military personnel of specified brigades")
    public ResponseEntity<Map<String, List<BrigadeDTO.MilitaryPersonnelDTO>>> createMilitaryPersonnel(@RequestBody @Valid MilitaryPersonnelCreationRequestDTO militaryPersonnelCreationRequestDTO) {
        return ResponseEntity.ok(militaryPersonnelService.createMilitaryPersonnel(militaryPersonnelCreationRequestDTO));
    }

    @PutMapping("/military-personnel/update")
    @Operation(description = "Update a military personnel of specified brigades")
    public ResponseEntity<List<BrigadeDTO.MilitaryPersonnelDTO>> updateMilitaryPersonnel(@RequestBody @Valid MilitaryPersonnelCreationRequestDTO militaryPersonnelCreationRequestDTO) {
        return ResponseEntity.ok(militaryPersonnelService.updateMilitaryPersonnel(militaryPersonnelCreationRequestDTO));
    }

    @DeleteMapping("/military-personnel/delete")
    @Operation(description = "Delete a military personnel by ids")
    public ResponseEntity<Void> deleteMilitaryPersonnel(@RequestParam final Set<Long> ids) {
        militaryPersonnelService.deleteMilitaryPersonnel(ids);
        return ResponseEntity.ok().build();
    }
}
