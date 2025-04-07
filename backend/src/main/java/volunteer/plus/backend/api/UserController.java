package volunteer.plus.backend.api;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import volunteer.plus.backend.domain.dto.UserInfo;
import volunteer.plus.backend.service.general.UserService;

@RestController
@RequestMapping(value = "/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(value = "/user-info")
    public ResponseEntity<UserInfo> getUserInfo() throws AuthenticationException {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

}
