package volunteer.plus.backend.service.general.impl;

import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import volunteer.plus.backend.domain.dto.UserInfo;
import volunteer.plus.backend.domain.entity.User;
import volunteer.plus.backend.repository.UserRepository;
import volunteer.plus.backend.service.general.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
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
    public boolean checkEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void createUser(User user) {
        String email = user.getEmail();

        if (checkEmail(email)) {
            throw new RuntimeException("User with email " + email + " was not found");
        }

        userRepository.saveAndFlush(user);
    }

    @Override
    public User getUserByResetToken(String resetToken) {
        return userRepository.findUserByResetToken(resetToken)
                .orElse(null);
    }

}
