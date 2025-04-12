package volunteer.plus.backend.domain.dto;

import com.drew.lang.annotations.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LiqPayCreationDTO {
    @NotNull
    private BigDecimal amount;

    // is used to update levy accumulated amount
    private Long levyId;
}
