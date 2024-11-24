package volunteer.plus.backend.domain.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LiqPayResponseDTO {
    private String data;
    private String signature;
}
