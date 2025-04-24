package volunteer.plus.backend.domain.dto.alarm;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import volunteer.plus.backend.domain.dto.TimeSpanDTO;
import volunteer.plus.backend.domain.enums.AlertType;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlertDurationResponseDTO {
    private String regionId;
    private String regionName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private TimeSpanDTO duration;
    private AlertType alertType;
    private boolean isContinue;
}
