package volunteer.plus.backend.domain.dto.stats;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WarStatsData {
    private String date;
    private int day;
    private String resource;
    @JsonProperty("war_status")
    private WarStatus warStatus;
    private Map<String, Integer> stats;
    private Map<String, Integer> increase;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WarStatus {
        @JsonProperty("code")
        private int code;

        @JsonProperty("alias")
        private String alias;
    }

}
