package volunteer.plus.backend.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import volunteer.plus.backend.domain.enums.CurrencyName;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true, exclude = "user")
@Entity
@Table(name = "liqpay_order")
public class LiqPayOrder extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private CurrencyName currencyName;

    private String orderId;

    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
