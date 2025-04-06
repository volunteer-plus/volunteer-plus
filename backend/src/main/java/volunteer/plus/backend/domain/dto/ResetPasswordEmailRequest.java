package volunteer.plus.backend.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class ResetPasswordEmailRequest {

    @Schema(description = "The email of the user", example = "user@example.com")
    private String email;

}
