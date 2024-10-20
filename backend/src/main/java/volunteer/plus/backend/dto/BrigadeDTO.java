package volunteer.plus.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrigadeDTO {
    private Long id;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    private String regimentCode;

    private String branch;

    private String role;

    private String partOf;

    private String websiteLink;

    private String currentCommander;

    private String description;

    private List<MilitaryPersonnelDTO> militaryPersonnel = new ArrayList<>();


    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MilitaryPersonnelDTO {
        private Long id;

        private LocalDateTime createDate;

        private LocalDateTime updateDate;

        private String firstName;

        private String lastName;

        private LocalDate dateOfBirth;

        private String placeOfBirth;

        private String rank;

        private String status;
    }
}
