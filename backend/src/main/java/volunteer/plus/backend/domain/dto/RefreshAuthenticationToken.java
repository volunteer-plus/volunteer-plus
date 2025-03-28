package volunteer.plus.backend.domain.dto;

import volunteer.plus.backend.domain.entity.User;

public class RefreshAuthenticationToken extends AbstractJwtAuthenticationToken {

    public RefreshAuthenticationToken(AccessJwtToken token) {
        super(token);
    }

    public RefreshAuthenticationToken(User user) {
        super(user);
    }

}
