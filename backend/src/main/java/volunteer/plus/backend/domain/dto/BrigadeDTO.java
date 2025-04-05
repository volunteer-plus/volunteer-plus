package volunteer.plus.backend.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import volunteer.plus.backend.domain.entity.MilitaryPersonnel;

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

    private String name;

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

        private Long userId;

        private LocalDateTime createDate;

        private LocalDateTime updateDate;

        private String firstName;

        private String lastName;

        private LocalDate dateOfBirth;

        private String placeOfBirth;

        private String rank;

        private String status;

        public static List<BrigadeDTO.MilitaryPersonnelDTO> mapMilitaryPersonnel(final List<MilitaryPersonnel> militaryPersonnel) {
            return militaryPersonnel.stream()
                    .map(person ->
                            BrigadeDTO.MilitaryPersonnelDTO.builder()
                                    .id(person.getId())
                                    .userId(person.getUser() == null ? null : person.getUser().getId())
                                    .createDate(person.getCreateDate())
                                    .updateDate(person.getUpdateDate())
                                    .firstName(person.getFirstName())
                                    .lastName(person.getLastName())
                                    .dateOfBirth(person.getDateOfBirth())
                                    .placeOfBirth(person.getPlaceOfBirth())
                                    .rank(person.getRank())
                                    .status(person.getStatus())
                                    .build()
                    )
                    .toList();
        }
    }
}
