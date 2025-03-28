package volunteer.plus.backend.domain.dto;

import groovy.transform.builder.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import volunteer.plus.backend.domain.entity.User;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;

    public UserInfo(User user) {
        this(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
    }
}
