package volunteer.plus.backend.domain.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginRequestDTO {

    private String username;

    private String password;

    @JsonCreator
    public LoginRequestDTO(@JsonProperty("email") String username, @JsonProperty("password") String password) {
        this.username = username;
        this.password = password;
    }

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "User email", example = "kurwa@org.ua")
    public String getUsername() {
        return username;
    }

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "User password", example = "tenant")
    public String getPassword() {
        return password;
    }
}
