package volunteer.plus.backend.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import volunteer.plus.backend.domain.enums.TwoFactorProvider;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user_2fa")
public class User2fa extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "method_type", nullable = false, length = 10)
    private TwoFactorProvider methodType;

    @Column(length = 100)
    private String secret;    // for TOTP

    @Column(length = 64)
    private String token;     // one-time code for SMS/Email

    @Column(name = "expiry_date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime expiryDate;

    @Column(nullable = false)
    private boolean used;

}
