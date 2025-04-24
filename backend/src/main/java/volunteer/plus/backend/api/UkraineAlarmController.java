package volunteer.plus.backend.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import volunteer.plus.backend.domain.dto.AlertDurationResponseDTO;
import volunteer.plus.backend.domain.dto.AlertRegionModelDTO;
import volunteer.plus.backend.domain.dto.RegionAlarmsHistoryDTO;
import volunteer.plus.backend.domain.dto.RegionsViewModelDTO;
import volunteer.plus.backend.service.alerts.AlarmService;

import java.time.LocalDate;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/no-auth")
@RequiredArgsConstructor
public class UkraineAlarmController {
    private final AlarmService alarmService;

    @GetMapping(value = "/alerts")
    @Operation(description = "Retrieve Ukraine alerts info")
    public ResponseEntity<List<AlertRegionModelDTO>> getAlerts(@RequestParam(required = false) final String regionId) {
        return ResponseEntity.ok(alarmService.getAlerts(regionId));
    }

    @GetMapping(value = "/alerts/regions")
    @Operation(description = "Retrieve Ukraine regions")
    public ResponseEntity<RegionsViewModelDTO> getRegions() {
        return ResponseEntity.ok(alarmService.getRegions());
    }

    @GetMapping(value = "/alerts/date-history")
    @Operation(description = "Retrieve Ukraine alerts date history info")
    public ResponseEntity<List<AlertDurationResponseDTO>> getDateHistoryAlerts(@RequestParam final LocalDate date) {
        return ResponseEntity.ok(alarmService.getDateHistoryAlerts(date));
    }

    @GetMapping(value = "/alerts/region-history")
    @Operation(description = "Retrieve Ukraine alerts region history info")
    public ResponseEntity<List<RegionAlarmsHistoryDTO>> getRegionHistoryAlerts(@RequestParam final String regionId) {
        return ResponseEntity.ok(alarmService.getRegionHistoryAlerts(regionId));
    }
}
