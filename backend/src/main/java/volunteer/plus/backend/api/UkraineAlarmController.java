package volunteer.plus.backend.api;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import volunteer.plus.backend.domain.dto.alarm.*;
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

    @PostMapping(value = "/alerts/webhook")
    @Operation(description = "Subscribe to service webhook")
    public void subscribeToWebhook(@RequestBody @Valid final WebHookModelDTO webHookModelDTO) {
        alarmService.subscribeToWebhook(webHookModelDTO);
    }

    @PatchMapping(value = "/alerts/webhook")
    @Operation(description = "Update subscription link to service webhook")
    public void updateSubscriptionToWebhook(@RequestBody @Valid final WebHookModelDTO webHookModelDTO) {
        alarmService.updateSubscriptionToWebhook(webHookModelDTO);
    }

    @DeleteMapping(value = "/alerts/webhook")
    @Operation(description = "Unsubscribe from service webhook")
    public void unsubscribeFromWebhook(@RequestBody @Valid final WebHookModelDTO webHookModelDTO) {
        alarmService.unsubscribeFromWebhook(webHookModelDTO);
    }

    @PostMapping("/alerts/handle")
    @Operation(description = "Webhook payload receiver")
    public void handleAlert(@RequestBody final AlertRegionModelDTO alert) {
        alarmService.handleAlert(alert);
    }
}
