package volunteer.plus.backend.api;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import volunteer.plus.backend.domain.dto.RegistrationData;
import volunteer.plus.backend.domain.dto.ResetPasswordEmailRequest;
import volunteer.plus.backend.domain.dto.ResetPasswordRequest;
import volunteer.plus.backend.service.auth.AuthService;
import volunteer.plus.backend.service.security.impl.VerificationTokenService;

import java.net.URI;

@RestController
@RequestMapping(value = "/api/no-auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final VerificationTokenService verificationTokenService;

    @PostMapping(value = "/registration")
    public ResponseEntity<Void> registration(@RequestBody RegistrationData registrationData) {
        authService.registerUser(registrationData);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/resetPasswordByEmail")
    public ResponseEntity<Void> requestResetPasswordByEmail(@RequestBody ResetPasswordEmailRequest resetPasswordByEmailRequest,
                                                            HttpServletRequest request) {
        authService.resetPasswordByEmail(resetPasswordByEmailRequest, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/resetPassword")
    public ResponseEntity<Void> checkResetToken(@RequestParam(value = "resetToken") String resetToken) {
        boolean isTokenValid = authService.checkResetToken(resetToken);

        String redirectUrl = isTokenValid
                ? "/login/resetPassword?resetToken=" + resetToken
                : "/passwordResetLinkExpired";
        URI uri = URI.create(redirectUrl);

        return ResponseEntity.ok()
                .location(uri)
                .build();
    }

    @PostMapping(value = "/resetPassword")
    public ResponseEntity<Void> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        boolean isPasswordChanged = authService.changePassword(resetPasswordRequest);
        if (isPasswordChanged) {
            URI loginUri = URI.create("/api/login");
            return ResponseEntity.status(HttpStatus.SEE_OTHER)
                    .location(loginUri)
                    .build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<String> confirmUserAccount(@RequestParam("token") String token) {
        verificationTokenService.confirmToken(token);
        return ResponseEntity.ok("Ваш акаунт успішно підтверджено!");
    }

}
