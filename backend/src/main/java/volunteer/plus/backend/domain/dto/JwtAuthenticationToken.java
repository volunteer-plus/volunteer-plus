package volunteer.plus.backend.domain.dto;

import volunteer.plus.backend.domain.entity.User;

public class JwtAuthenticationToken extends AbstractJwtAuthenticationToken {

    public JwtAuthenticationToken(AccessJwtToken token) {
        super(token);
    }

    public JwtAuthenticationToken(User user) {
        super(user);
    }
}
