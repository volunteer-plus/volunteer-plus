package volunteer.plus.backend.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LevyCreationDTO {
    private Long id;

    private BigDecimal accumulated;

    private String trophyDescription;

    private Set<Long> volunteerIds = new HashSet<>();
}
