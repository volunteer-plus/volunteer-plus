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

    @Column(nullable = false)
    private boolean enabled = false;

    @Column(name = "total_allowed_time_sec", nullable = false)
    private long totalAllowedTimeSec = 3600;

    @Column(name = "retry_period_sec", nullable = false)
    private long retryPeriodSec = 30;

    @Column(name = "max_failures", nullable = false)
    private int maxFailures = 5;

    @Column(name = "attempts_count", nullable = false)
    private int attemptsCount = 0;

    @Column(name = "last_code_sent_at")
    private LocalDateTime lastCodeSentAt;

    // --- нове поле ---
    @Column(name = "verification_id", nullable = false, unique = true, length = 36)
    private String verificationId;

}
