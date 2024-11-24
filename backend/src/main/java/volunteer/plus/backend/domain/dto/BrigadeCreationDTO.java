package volunteer.plus.backend.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrigadeCreationDTO {
    @NotNull
    @NotBlank
    private String regimentCode;

    private String name;

    private String branch;

    private String role;

    private String partOf;

    private String websiteLink;

    private String currentCommander;

    private String description;
}
