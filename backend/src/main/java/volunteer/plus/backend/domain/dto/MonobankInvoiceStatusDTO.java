package volunteer.plus.backend.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonobankInvoiceStatusDTO {

    private String invoiceId;
    private String status;
    private String failureReason;
}
