package volunteer.plus.backend.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import volunteer.plus.backend.domain.dto.LoginData;
import volunteer.plus.backend.domain.dto.RegistrationData;
import volunteer.plus.backend.domain.dto.TokenResponse;
import volunteer.plus.backend.service.general.impl.AuthenticationServiceImpl;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthorizeController {

    private final AuthenticationServiceImpl authenticationServiceImpl;

    @PostMapping("/registration")
    public ResponseEntity<String> registration(@RequestBody RegistrationData registrationData){
        return ResponseEntity.ok(authenticationServiceImpl.registration(registrationData));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginData loginData){
        return ResponseEntity.ok(authenticationServiceImpl.login(loginData));
    }
}
