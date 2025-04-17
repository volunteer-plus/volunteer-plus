package volunteer.plus.backend.domain.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {

    SUPER_ADMIN,
    BRIGADE_ADMIN,
    VOLUNTEER,
    MILITARY;

    @Override
    public String getAuthority() {
        return this.name();
    }

}
