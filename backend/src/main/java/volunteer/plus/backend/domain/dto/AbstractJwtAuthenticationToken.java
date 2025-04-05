package volunteer.plus.backend.domain.dto;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import volunteer.plus.backend.domain.entity.User;

abstract class AbstractJwtAuthenticationToken extends AbstractAuthenticationToken {

    private AccessJwtToken accessToken;
    private User user;

    public AbstractJwtAuthenticationToken(AccessJwtToken token) {
        super(null);
        this.accessToken = token;
        this.setAuthenticated(false);
    }

    public AbstractJwtAuthenticationToken(User user) {
        super(user.getAuthorities());
        this.eraseCredentials();
        this.user = user;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return accessToken;
    }

    @Override
    public Object getPrincipal() {
        return user;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.accessToken = null;
    }

}
