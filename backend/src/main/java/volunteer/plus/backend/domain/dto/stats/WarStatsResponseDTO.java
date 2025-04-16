package volunteer.plus.backend.domain.dto.stats;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WarStatsResponseDTO {
    private String message;
    private WarStatsData data;
}
