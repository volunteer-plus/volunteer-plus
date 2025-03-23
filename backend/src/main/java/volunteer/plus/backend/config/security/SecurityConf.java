package volunteer.plus.backend.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfigurationSource;
import volunteer.plus.backend.service.general.UserService;
import volunteer.plus.backend.service.security.JwtTokenExtractor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConf {

    private final UserService userService;
    private final AuthenticationSuccessHandler successHandler;
    private final AuthenticationFailureHandler failureHandler;
    private final AuthenticationManager authenticationManager;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final RefreshTokenAuthenticationProvider refreshTokenAuthenticationProvider;
    private final JwtTokenExtractor jwtTokenExtractor;
    private final CorsConfigurationSource corsConfigurationSource;
    public static final String FORM_BASED_LOGIN_ENTRY_POINT = "/api/login";
    public static final String TOKEN_REFRESH_ENTRY_POINT = "/api/auth/token";
    public static final String TOKEN_BASED_AUTH_ENTRY_POINT = "/api/**";

    @Bean
    public DaoAuthenticationProvider buildRestAuthenticationProvider() {
        var user = new DaoAuthenticationProvider();
        user.setPasswordEncoder(passwordEncoder());
        user.setUserDetailsService(userDetailsService());
        return user;
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return userService::getUserByEmail;
    }

    @Bean
    protected RestLoginProcessingFilter buildRestLoginProcessingFilter() throws Exception {
        RestLoginProcessingFilter filter = new RestLoginProcessingFilter(FORM_BASED_LOGIN_ENTRY_POINT, userService, successHandler, failureHandler);
        filter.setAuthenticationManager(this.authenticationManager);
        return filter;
    }

    @Bean
    protected JwtTokenAuthenticationFilter buildJwtTokenAuthenticationProcessingFilter() throws Exception {
        List<RequestMatcher> pathsToSkip = Stream.of(TOKEN_REFRESH_ENTRY_POINT, FORM_BASED_LOGIN_ENTRY_POINT)
                .map(AntPathRequestMatcher::new)
                .collect(Collectors.toList());

        OrRequestMatcher skipMatchers = new OrRequestMatcher(pathsToSkip);
        JwtTokenAuthenticationFilter filter
                = new JwtTokenAuthenticationFilter(failureHandler, jwtTokenExtractor, new NegatedRequestMatcher(skipMatchers));

        filter.setAuthenticationManager(this.authenticationManager);
        return filter;
    }

    @Bean
    protected RefreshTokenProcessingFilter buildRefreshTokenProcessingFilter() throws Exception {
        RefreshTokenProcessingFilter filter = new RefreshTokenProcessingFilter(TOKEN_REFRESH_ENTRY_POINT, successHandler, failureHandler);
        filter.setAuthenticationManager(this.authenticationManager);
        return filter;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        List<AuthenticationProvider> providers = Arrays.asList(
                buildRestAuthenticationProvider(), jwtAuthenticationProvider, refreshTokenAuthenticationProvider
        );
        return new ProviderManager(providers);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(e -> e.configurationSource(corsConfigurationSource))
                .authorizeHttpRequests(e -> e
                        .requestMatchers(
                                FORM_BASED_LOGIN_ENTRY_POINT,
                                TOKEN_REFRESH_ENTRY_POINT
                        )
                        .permitAll()
                        .requestMatchers(TOKEN_BASED_AUTH_ENTRY_POINT)
                        .authenticated()
                )
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(buildRestLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(buildJwtTokenAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(buildRefreshTokenProcessingFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
