package volunteer.plus.backend.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import volunteer.plus.backend.domain.enums.CurrencyName;
import volunteer.plus.backend.domain.enums.PaymentProvider;
import volunteer.plus.backend.domain.enums.PaymentStatus;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true, exclude = "user")
@Entity
@Table(name = "payment_order")
public class PaymentOrder extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private CurrencyName currencyName;

    private String orderId;

    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /** Хто обробляє платіж */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentProvider provider;

    /** Статус самого платежу */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

}
