package volunteer.plus.backend.service.general;

import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import volunteer.plus.backend.domain.dto.RegistrationData;
import volunteer.plus.backend.domain.dto.UserInfo;

public interface UserService extends UserDetailsService {

    UserInfo getCurrentUser() throws AuthenticationException;

    void registerUser(RegistrationData registrationData);

    boolean checkEmail(String email);

    PasswordEncoder getPasswordEncoder();
}
