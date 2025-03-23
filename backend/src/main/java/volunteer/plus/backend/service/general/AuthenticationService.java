package volunteer.plus.backend.service.general;

import volunteer.plus.backend.domain.dto.LoginRequestDTO;
import volunteer.plus.backend.domain.dto.RegistrationData;
import volunteer.plus.backend.domain.dto.TokenPairResponse;

public interface AuthenticationService {
    String registration(RegistrationData registrationData);
    TokenPairResponse login(LoginRequestDTO loginRequestDTO);
}
