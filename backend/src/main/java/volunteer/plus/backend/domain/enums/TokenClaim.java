package volunteer.plus.backend.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TokenClaim {

    ROLES("authority"),
    USER_ID("user_id");

    private final String name;

}
