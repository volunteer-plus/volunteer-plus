package volunteer.plus.backend.service.general.impl;

import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import volunteer.plus.backend.domain.dto.RegistrationData;
import volunteer.plus.backend.domain.dto.UserInfo;
import volunteer.plus.backend.domain.entity.User;
import volunteer.plus.backend.repository.UserRepository;
import volunteer.plus.backend.service.general.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public UserInfo getCurrentUser() throws AuthenticationException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            User user =  (User) authentication.getPrincipal();
            return new UserInfo(user);
        } else {
            throw new AuthenticationException("You aren't authorized to perform this operation!");
        }
    }

    public User loadUserByUsername(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User with email " + email + " was not found"));
    }

    @Override
    public void registerUser(RegistrationData registrationData) {
        String email = registrationData.getEmail();

        if (checkEmail(email)) {
            throw new RuntimeException("User with email " + email + " was not found");
        }

        var user = User
                .builder()
                .enabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .email(email)
                .password(encodePassword(registrationData.getPassword()))
                .firstName(registrationData.getFirstName())
                .lastName(registrationData.getLastName())
                .build();

        userRepository.saveAndFlush(user);
    }

    @Override
    public boolean checkEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

}
