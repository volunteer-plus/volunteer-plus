package volunteer.plus.backend.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import volunteer.plus.backend.domain.enums.RequestStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestDTO {
    private Long id;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    @NotNull
    private String description;

    @NotNull
    private LocalDateTime deadline;

    @NotNull
    private BigDecimal amount;

    private BigDecimal accumulated;

    private String brigade;

    private String category;

    private RequestStatus status;
}
