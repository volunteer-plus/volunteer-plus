package volunteer.plus.backend.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
