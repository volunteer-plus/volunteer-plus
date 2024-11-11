package volunteer.plus.backend.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VolunteerDTO {
    private Long id;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    private BigDecimal reputationScore;

    private String firstName;

    private String lastName;

    private List<VolunteerFeedbackDTO> volunteerFeedbacks = new ArrayList<>();

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class VolunteerFeedbackDTO {
        private Long id;

        private LocalDateTime createDate;

        private LocalDateTime updateDate;

        private BigDecimal reputationScore;

        private String author;

        private String text;
    }
}
