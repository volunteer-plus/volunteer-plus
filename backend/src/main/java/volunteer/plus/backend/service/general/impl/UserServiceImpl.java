package volunteer.plus.backend.service.general.impl;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import volunteer.plus.backend.domain.entity.User;
import volunteer.plus.backend.repository.UserRepository;
import volunteer.plus.backend.service.general.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User getCurrentUser() throws AuthenticationException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return (User) authentication.getPrincipal();
        } else {
            throw new AuthenticationException("You aren't authorized to perform this operation!");
        }
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User with email " + email + " was not found"));
    }

}
