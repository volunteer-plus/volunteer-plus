package volunteer.plus.backend.domain.dto.alarm;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import volunteer.plus.backend.domain.enums.AlertType;
import volunteer.plus.backend.domain.enums.RegionType;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlertDTO {
    private String regionId;
    private RegionType regionType;
    private AlertType type;
    private LocalDateTime lastUpdate;
}
