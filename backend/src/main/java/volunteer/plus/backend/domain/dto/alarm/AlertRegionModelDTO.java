package volunteer.plus.backend.domain.dto.alarm;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import volunteer.plus.backend.domain.enums.RegionType;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlertRegionModelDTO {
    private String regionId;
    private RegionType regionType;
    private String regionName;
    private String regionEngName;
    private LocalDateTime lastUpdate;
    private List<AlertDTO> activeAlerts;
}
