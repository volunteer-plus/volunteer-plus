package volunteer.plus.backend.api;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import volunteer.plus.backend.domain.dto.VolunteerDTO;
import volunteer.plus.backend.domain.dto.VolunteerFeedbackPayloadDTO;
import volunteer.plus.backend.service.general.VolunteerService;

import java.util.Set;

@Validated
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class VolunteerController {
    private final VolunteerService volunteerService;

    @GetMapping("/volunteers")
    @Operation(description = "Retrieve all volunteers")
    public ResponseEntity<Page<VolunteerDTO>> getVolunteers(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size,
                                                            @RequestParam(required = false) final Set<Long> volunteerIds) {
        return ResponseEntity.ok(volunteerService.getVolunteers(PageRequest.of(page, size), volunteerIds));
    }

    @PostMapping("/volunteer/create")
    @Operation(description = "Create a volunteers")
    public ResponseEntity<VolunteerDTO> createVolunteer(@RequestParam final String userEmail) {
        return ResponseEntity.ok(volunteerService.createVolunteer(userEmail));
    }

    @PostMapping("/volunteer/create-or-update-feedback")
    @Operation(description = "Create or update a feedback for a volunteer")
    public ResponseEntity<VolunteerDTO> createOrUpdateFeedback(@RequestParam final String userEmail,
                                                               @RequestBody @Valid final VolunteerFeedbackPayloadDTO volunteerFeedbackPayloadDTO) {
        return ResponseEntity.ok(volunteerService.createOrUpdateFeedback(userEmail, volunteerFeedbackPayloadDTO));
    }

    @PostMapping("/volunteer/remove-feedback")
    @Operation(description = "Remove a feedback for a volunteer")
    public ResponseEntity<Void> removeFeedback(@RequestParam final Long feedbackId) {
        volunteerService.removeFeedback(feedbackId);
        return ResponseEntity.ok().build();
    }
}
