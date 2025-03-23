package volunteer.plus.backend.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import volunteer.plus.backend.domain.dto.LoginRequestDTO;
import volunteer.plus.backend.domain.dto.RegistrationData;
import volunteer.plus.backend.domain.dto.TokenPairResponse;
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
    public ResponseEntity<TokenPairResponse> login(@RequestBody LoginRequestDTO loginRequestDTO){
        return ResponseEntity.ok(authenticationServiceImpl.login(loginRequestDTO));
    }
}
