package volunteer.plus.backend.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Verify2faRequestDto {

    @NotBlank
    private String verificationId;
    @NotBlank
    private String code;

}
