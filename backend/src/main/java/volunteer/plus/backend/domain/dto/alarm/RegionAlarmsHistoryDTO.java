package volunteer.plus.backend.domain.dto.alarm;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegionAlarmsHistoryDTO {
    private String regionId;
    private String regionName;
    private List<AlertDurationResponseDTO> alarms;
}
