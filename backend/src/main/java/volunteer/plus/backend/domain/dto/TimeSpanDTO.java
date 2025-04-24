package volunteer.plus.backend.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import volunteer.plus.backend.util.jackson.TimeSpanDTODeserializer;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(using = TimeSpanDTODeserializer.class)
public class TimeSpanDTO {
    private long ticks;
    private int days;
    private int hours;
    private int milliseconds;
    private int minutes;
    private int seconds;
    private double totalDays;
    private double totalHours;
    private double totalMilliseconds;
    private double totalMinutes;
    private double totalSeconds;
}
