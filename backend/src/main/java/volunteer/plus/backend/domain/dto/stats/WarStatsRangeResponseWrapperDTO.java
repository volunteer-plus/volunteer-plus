package volunteer.plus.backend.domain.dto.stats;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WarStatsRangeResponseWrapperDTO {
    private List<WarStatsData> records;
}
