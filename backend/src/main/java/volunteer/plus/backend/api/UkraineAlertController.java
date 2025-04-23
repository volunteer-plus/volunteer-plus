package volunteer.plus.backend.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import volunteer.plus.backend.domain.dto.AlertRegionModelDTO;
import volunteer.plus.backend.service.alerts.AlertService;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/no-auth")
@RequiredArgsConstructor
public class UkraineAlertController {
    private final AlertService alertService;

    @GetMapping(value = "/alerts")
    @Operation(description = "Retrieve Ukraine alerts info")
    public ResponseEntity<List<AlertRegionModelDTO>> getAlerts() {
        return ResponseEntity.ok(alertService.getAlerts());
    }
}
