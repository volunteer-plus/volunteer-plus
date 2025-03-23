package volunteer.plus.backend.service.general.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import volunteer.plus.backend.domain.dto.LoginRequestDTO;
import volunteer.plus.backend.domain.dto.RegistrationData;
import volunteer.plus.backend.domain.dto.TokenPairResponse;
import volunteer.plus.backend.domain.entity.User;
import volunteer.plus.backend.repository.UserRepository;
import volunteer.plus.backend.service.email.EmailNotificationBuilderService;
import volunteer.plus.backend.service.general.AuthenticationService;
import volunteer.plus.backend.service.security.impl.JwtServiceIml;

@Component
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtServiceIml jwtServiceIml;
    private final EmailNotificationBuilderService emailNotificationBuilderService;

    @Override
    public String registration(RegistrationData registrationData) {
        var user = User
                .builder()
                .enabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .email(registrationData.getEmail())
                .password(passwordEncoder.encode(registrationData.getPassword()))
                .firstName(registrationData.getName())
                .lastName(registrationData.getLastname())
                .build();

        userRepository.saveAndFlush(user);

        emailNotificationBuilderService.createUserRegistrationEmail(user);

        return "You have registered";
    }

    @Override
    @Transactional
    public TokenPairResponse login(LoginRequestDTO loginRequestDTO) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getEmail(),
                        loginRequestDTO.getPassword()
                )
        );
        var user = userRepository.findUserByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("User was not found"));

        return TokenPairResponse
                .builder()
                .token(jwtServiceIml.generateToken(user))
                .build();
    }
}
