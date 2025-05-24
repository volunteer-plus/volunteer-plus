package volunteer.plus.backend.service.auth.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import volunteer.plus.backend.domain.dto.TokenPairResponse;
import volunteer.plus.backend.domain.entity.User;
import volunteer.plus.backend.repository.UserRepository;
import volunteer.plus.backend.service.general.UserService;
import volunteer.plus.backend.service.security.TokenFactoryService;

import java.io.IOException;

@Service(value = "oauth2_seccess")
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final TokenFactoryService tokenFactory;
    private final UserService userService;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        User securityUser = (User) authentication.getPrincipal();

        TokenPairResponse tokenPair = tokenFactory.generateTokenPair(securityUser);

        CookieUtils.addCookie(response, "access_token", tokenPair.getAccessToken(), 3600);        // 1 година
        CookieUtils.addCookie(response, "refresh_token", tokenPair.getRefreshToken(), 7 * 24 * 3600); // 7 днів

        new HttpCookieOAuth2AuthorizationRequestRepository()
                .removeAuthorizationRequest(request, response);

        response.sendRedirect("/api/no-auth/home");
    }
}