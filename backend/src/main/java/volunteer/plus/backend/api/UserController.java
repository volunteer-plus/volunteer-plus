package volunteer.plus.backend.api;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import volunteer.plus.backend.domain.dto.RegistrationData;
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

    @PostMapping(value = "/registration")
    public ResponseEntity<Void> registration(@RequestBody RegistrationData registrationData){
        userService.registerUser(registrationData);
        return ResponseEntity.ok().build();
    }

}
