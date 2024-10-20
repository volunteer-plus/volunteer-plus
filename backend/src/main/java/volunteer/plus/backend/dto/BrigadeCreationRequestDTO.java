package volunteer.plus.backend.dto;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrigadeCreationRequestDTO {
    @Valid
    private List<BrigadeCreationDTO> brigades = new ArrayList<>();
}
