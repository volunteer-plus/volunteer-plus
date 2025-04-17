package volunteer.plus.backend.domain.dto;

import groovy.transform.builder.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import volunteer.plus.backend.domain.entity.User;

import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {

    private Long id;
    private String firstName;
    private String middleName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String logoS3Link;
    private String logoFilename;
    private String phoneNumber;
    private String email;
    private String role;

    public UserInfo(final User user) {
        this(user.getId(), user.getFirstName(), user.getMiddleName(),
                user.getLastName(), user.getDateOfBirth(), user.getLogoS3Link(),
                user.getLogoFilename(), user.getPhoneNumber(), user.getEmail(), user.getRole().getAuthority());
    }
}
