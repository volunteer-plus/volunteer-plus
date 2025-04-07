package volunteer.plus.backend.domain.dto;

import lombok.Data;

@Data
public class ResetPasswordRequest {

    private String resetToken;
    private String password;

}
