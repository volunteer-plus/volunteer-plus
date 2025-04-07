package volunteer.plus.backend.service.auth;

import jakarta.servlet.http.HttpServletRequest;
import volunteer.plus.backend.domain.dto.RegistrationData;
import volunteer.plus.backend.domain.dto.ResetPasswordEmailRequest;
import volunteer.plus.backend.domain.dto.ResetPasswordRequest;

public interface AuthService {

    void registerUser(RegistrationData registrationData);

    void resetPasswordByEmail(ResetPasswordEmailRequest resetPasswordByEmailRequest,
                              HttpServletRequest request);

    boolean checkResetToken(String resetToken);

    boolean changePassword(ResetPasswordRequest resetPasswordRequest);

}
