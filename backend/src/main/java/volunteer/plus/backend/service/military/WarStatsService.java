package volunteer.plus.backend.service.military;

import volunteer.plus.backend.domain.dto.stats.WarStatsRangeResponseDTO;
import volunteer.plus.backend.domain.dto.stats.WarStatsResponseDTO;

import java.time.LocalDate;

public interface WarStatsService {
    WarStatsResponseDTO getWarStats(LocalDate date);

    WarStatsRangeResponseDTO getWarStatsRange(LocalDate dateFrom, LocalDate dateTo);
}
