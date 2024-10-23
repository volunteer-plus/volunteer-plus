package volunteer.plus.backend.domain.dto;

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
public class LevyDTO {
    private Long id;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    private BigDecimal accumulated;

    private String trophyDescription;
}
