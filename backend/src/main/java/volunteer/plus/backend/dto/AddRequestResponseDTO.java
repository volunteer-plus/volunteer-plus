package volunteer.plus.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddRequestResponseDTO {
    private Long id;
    private String code;
    private boolean executed;
}
