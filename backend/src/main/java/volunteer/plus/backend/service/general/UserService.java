package volunteer.plus.backend.service.general;

import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;
import volunteer.plus.backend.domain.dto.RegistrationData;
import volunteer.plus.backend.domain.dto.UserInfo;
import volunteer.plus.backend.domain.entity.PasswordResetToken;
import volunteer.plus.backend.domain.entity.User;

import java.util.Optional;

public interface UserService extends UserDetailsService {

    UserInfo getCurrentUser() throws AuthenticationException;

    UserInfo generalInfoUpdate(User user, RegistrationData registrationData);

    UserInfo uploadLogo(User user, MultipartFile multipartFile);

    ResponseEntity<byte[]> downloadLogo(User user);

    boolean checkEmail(String email);

    void createUser(User user);

    User getUserByResetToken(String resetToken);

    PasswordResetToken getResetToken(String token);

    void setPasswordResetToken(User user, String token);

    Optional<User> findByEmail(String email);

    void deleteUser(User user);

}
