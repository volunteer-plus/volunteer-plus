package volunteer.plus.backend.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonobankCreateInvoiceDTO {

    @NotNull
    private BigDecimal amount;
    private Integer ccy;

    @NotBlank
    private String webHookUrl;

    //private MerchantPaymentInfoDTO merchantPaymInfo;
}
