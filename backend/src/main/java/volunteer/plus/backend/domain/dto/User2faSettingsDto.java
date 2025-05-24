package volunteer.plus.backend.domain.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;
import volunteer.plus.backend.domain.entity.User2fa;

import java.time.ZoneOffset;

@Data
public class User2faSettingsDto {
    @Min(60)
    private long totalAllowedTimeSec;
    @Min(1)
    private long retryPeriodSec;
    @Min(1)
    private int maxFailures;

    public static User2faSettingsDto fromEntity(User2fa e) {
        User2faSettingsDto d = new User2faSettingsDto();
        d.setTotalAllowedTimeSec(e.getExpiryDate().toInstant(ZoneOffset.UTC).toEpochMilli());
        d.setRetryPeriodSec(e.getRetryPeriodSec());
        d.setMaxFailures(e.getMaxFailures());
        return d;
    }
}