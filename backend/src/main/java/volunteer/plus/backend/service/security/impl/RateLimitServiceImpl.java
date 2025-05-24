package volunteer.plus.backend.service.security.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import volunteer.plus.backend.service.general.CookieService;
import volunteer.plus.backend.service.security.RateLimitService;
import volunteer.plus.backend.service.general.UserService;
import volunteer.plus.backend.service.security.JwtTokenExtractor;
import volunteer.plus.backend.service.security.TokenFactoryService;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RateLimitServiceImpl implements RateLimitService {

    private static final String COOKIE_NAME = "RL_ID";
    private static final String ANON_PREFIX = "anon:";

    private final CookieService cookieService;
    private final UserService userService;
    private final TokenFactoryService tokenProvider;
    private final JwtTokenExtractor jwtTokenExtractor;

    @Override
    public String resolveKey(HttpServletRequest request, HttpServletResponse response) {
        Optional<String> userId = resolveUserUuidFromToken(request);
        if (userId.isPresent()) {
            return userId.get();
        }

        String uuid = cookieService.getOrCreateCookie(
                request,
                response,
                COOKIE_NAME,
                () -> UUID.randomUUID().toString(),
                Duration.ofDays(120),
                true,
                request.isSecure(),
                "/"
        );
        return ANON_PREFIX + uuid;
    }

    private Optional<String> resolveUserUuidFromToken(HttpServletRequest request) {
        String token = jwtTokenExtractor.extract(request);

        if (token != null) {
            String username = tokenProvider.extractUsername(token);
            UserDetails userDetails = userService.loadUserByUsername(username);
            if (tokenProvider.isTokenValid(token, userDetails)) {
                return Optional.of(userDetails.getUsername());
            }
        }

        return Optional.empty();
    }

}
