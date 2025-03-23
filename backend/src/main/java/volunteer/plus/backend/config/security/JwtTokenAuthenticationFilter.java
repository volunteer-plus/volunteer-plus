package volunteer.plus.backend.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;
import volunteer.plus.backend.domain.dto.AccessJwtToken;
import volunteer.plus.backend.domain.dto.JwtAuthenticationToken;
import volunteer.plus.backend.service.security.JwtTokenExtractor;

import java.io.IOException;

public class JwtTokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final AuthenticationFailureHandler failureHandler;
    private final JwtTokenExtractor jwtTokenExtractor;

    public JwtTokenAuthenticationFilter(AuthenticationFailureHandler failureHandler,
                                        JwtTokenExtractor jwtTokenExtractor, RequestMatcher matcher) {
        super(matcher);
        this.failureHandler = failureHandler;
        this.jwtTokenExtractor = jwtTokenExtractor;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        AccessJwtToken token = new AccessJwtToken(jwtTokenExtractor.extract(request));
        return getAuthenticationManager().authenticate(new JwtAuthenticationToken(token));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);
        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        failureHandler.onAuthenticationFailure(request, response, failed);
    }
}
