package volunteer.plus.backend.config.security;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import volunteer.plus.backend.domain.dto.AccessJwtToken;
import volunteer.plus.backend.domain.dto.RefreshAuthenticationToken;
import volunteer.plus.backend.domain.entity.User;
import volunteer.plus.backend.service.general.UserService;
import volunteer.plus.backend.service.security.TokenFactoryService;

@Component
@RequiredArgsConstructor
public class RefreshTokenAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;
    private final TokenFactoryService tokenFactoryService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        AccessJwtToken rawAccessToken = (AccessJwtToken) authentication.getCredentials();

        if (StringUtils.isEmpty(rawAccessToken.getToken())) {
            throw new BadCredentialsException("Token is invalid");
        }

        String username = tokenFactoryService.extractUsername(rawAccessToken.getToken());
        User user = (User) userService.loadUserByUsername(username);

        if (tokenFactoryService.isTokenValid(rawAccessToken.getToken(), user)) {
            throw new BadCredentialsException("Token is expired");
        }

        return new RefreshAuthenticationToken(user);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return RefreshAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
