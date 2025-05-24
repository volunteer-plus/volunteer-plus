package volunteer.plus.backend.domain.dto;

import com.drew.lang.annotations.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import volunteer.plus.backend.domain.enums.CurrencyName;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCreationDTO {
    @NotNull
    private BigDecimal amount;

    private CurrencyName currency;
    // is used to update levy accumulated amount
    private Long levyId;
}
