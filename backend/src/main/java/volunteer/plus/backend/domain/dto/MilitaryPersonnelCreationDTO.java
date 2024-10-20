package volunteer.plus.backend.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MilitaryPersonnelCreationDTO {
    @NotNull
    private String regimentCode;

    @NotNull
    private String addRequestId;

    @NotNull
    private List<MilitaryPersonnelDTO> militaryPersonnel = new ArrayList<>();

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MilitaryPersonnelDTO {
        private Long id;
        private String firstName;
        private String lastName;
        private LocalDate dateOfBirth;
        private String placeOfBirth;
        private String rank;
        private String status;
    }
}
