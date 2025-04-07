package volunteer.plus.backend.service.general;

import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import volunteer.plus.backend.domain.dto.UserInfo;
import volunteer.plus.backend.domain.entity.User;

public interface UserService extends UserDetailsService {

    UserInfo getCurrentUser() throws AuthenticationException;

    boolean checkEmail(String email);

    void createUser(User user);

    User getUserByResetToken(String resetToken);

}
