package volunteer.plus.backend.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "verification_tokens", indexes = {
        @Index(name = "idx_verif_user", columnList = "user_id"),
        @Index(name = "idx_verif_expiry", columnList = "expiry_date")
})
public class VerificationToken extends BaseEntity {

    @Column(name = "token", length = 64, nullable = false, unique = true)
    private String token;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "expiry_date", nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime expiryDate;

}
