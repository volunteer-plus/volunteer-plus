package volunteer.plus.backend.domain.dto;

import com.drew.lang.annotations.NotNull;
import lombok.Data;
import volunteer.plus.backend.domain.entity.User2fa;
import volunteer.plus.backend.domain.enums.TwoFactorProvider;

@Data
public class User2faConfigDto {
    private boolean enabled;
    @NotNull
    private TwoFactorProvider methodType;

    public static User2faConfigDto fromEntity(User2fa e) {
        User2faConfigDto d = new User2faConfigDto();
        d.setEnabled(e.isEnabled());
        d.setMethodType(e.getMethodType());
        return d;
    }
}
