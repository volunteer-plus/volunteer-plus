package volunteer.plus.backend.service.general;

import org.apache.tomcat.websocket.AuthenticationException;
import volunteer.plus.backend.domain.entity.User;

public interface UserService {

    User getCurrentUser() throws AuthenticationException;

    User getUserByEmail(String email);

}
