package volunteer.plus.backend.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VolunteerFeedbackPayloadDTO {
    @NotNull
    private Long volunteerId;
    private Long feedbackId;
    private String text;
    @NotNull
    private BigDecimal score;
}
