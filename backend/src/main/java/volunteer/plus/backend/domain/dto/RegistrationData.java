package volunteer.plus.backend.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import volunteer.plus.backend.service.security.impl.ValidPassword;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationData {

    // fields which should not be updated cia standard API
    private String email;

    @ValidPassword
    private String password;

    // general info fields
    private String firstName;
    private String lastName;
    private String middleName;
    private String phoneNumber;
    private LocalDate dateOfBirth;

}
