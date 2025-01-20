package volunteer.plus.backend.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageGenerationRequestDTO {
    @NotNull
    private String quality;
    @NotNull
    private Integer number;
    @NotNull
    private Integer height;
    @NotNull
    private Integer width;
    @NotNull
    private String prompt;
}
