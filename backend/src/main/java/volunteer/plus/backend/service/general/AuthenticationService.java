package volunteer.plus.backend.service.general;

import volunteer.plus.backend.domain.dto.LoginData;
import volunteer.plus.backend.domain.dto.RegistrationData;
import volunteer.plus.backend.domain.dto.TokenResponse;

public interface AuthenticationService {
    String registration(RegistrationData registrationData);
    TokenResponse login(LoginData loginData);
}
