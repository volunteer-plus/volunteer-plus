package volunteer.plus.backend.domain.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TokenPairResponse {

    private String accessToken;
    private String refreshToken;

}
