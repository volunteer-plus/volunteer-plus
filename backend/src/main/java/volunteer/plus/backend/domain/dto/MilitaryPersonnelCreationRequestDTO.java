package volunteer.plus.backend.domain.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MilitaryPersonnelCreationRequestDTO {
    @Valid
    @NotNull
    private List<MilitaryPersonnelCreationDTO> militaryPersonnel = new ArrayList<>();
}
