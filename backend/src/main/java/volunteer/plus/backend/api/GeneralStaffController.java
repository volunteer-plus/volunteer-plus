package volunteer.plus.backend.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import volunteer.plus.backend.domain.dto.stats.WarStatsRangeResponseDTO;
import volunteer.plus.backend.domain.dto.stats.WarStatsResponseDTO;
import volunteer.plus.backend.service.military.WarStatsService;

import java.time.LocalDate;

@Validated
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class GeneralStaffController {
    private final WarStatsService warStatsService;

    @GetMapping("/war-stats")
    public ResponseEntity<WarStatsResponseDTO> getWarStats(@RequestParam(required = false) final LocalDate date) {
        return ResponseEntity.ok(warStatsService.getWarStats(date));
    }

    @GetMapping("/war-stats-range")
    public ResponseEntity<WarStatsRangeResponseDTO> getWarStatsRange(@RequestParam final LocalDate dateFrom,
                                                                     @RequestParam final LocalDate dateTo) {
        return ResponseEntity.ok(warStatsService.getWarStatsRange(dateFrom, dateTo));
    }
}
