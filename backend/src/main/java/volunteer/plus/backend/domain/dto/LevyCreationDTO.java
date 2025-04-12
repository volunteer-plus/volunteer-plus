package volunteer.plus.backend.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import volunteer.plus.backend.domain.enums.LevyStatus;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LevyCreationDTO {
    private Long id;

    @NotNull
    private BigDecimal accumulated;

    private String trophyDescription;

    private String description;

    @NotNull
    private BigDecimal goalAmount;

    private String category;

    private LevyStatus status;

    private Set<Long> volunteerIds = new HashSet<>();
}
