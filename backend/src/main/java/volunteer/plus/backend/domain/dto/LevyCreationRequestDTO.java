package volunteer.plus.backend.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LevyCreationRequestDTO {
    @NotNull
    @NotEmpty
    private List<LevyCreationDTO> levies = new ArrayList<>();
}
