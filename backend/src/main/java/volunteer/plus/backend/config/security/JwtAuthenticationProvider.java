package volunteer.plus.backend.config.security;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import volunteer.plus.backend.domain.dto.AccessJwtToken;
import volunteer.plus.backend.domain.dto.JwtAuthenticationToken;
import volunteer.plus.backend.domain.entity.User;
import volunteer.plus.backend.service.general.UserService;
import volunteer.plus.backend.service.security.TokenFactoryService;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final TokenFactoryService tokenFactory;
    private final UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        AccessJwtToken rawAccessToken = (AccessJwtToken) authentication.getCredentials();
        String token = rawAccessToken.getToken();

        if (StringUtils.isEmpty(token)) {
            throw new BadCredentialsException("Token is invalid");
        }

        String email = tokenFactory.extractUsername(token);
        User user = (User) userService.loadUserByUsername(email);

        if(!tokenFactory.isTokenValid(token, user)) {
            throw new BadCredentialsException("Token is invalid");
        }

        return new JwtAuthenticationToken(user);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }

}
