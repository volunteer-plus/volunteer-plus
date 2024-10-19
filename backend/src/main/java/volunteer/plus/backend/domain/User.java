package volunteer.plus.backend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true, exclude = {"volunteer", "requests"})
@Entity
@Table(name = "user")
public class User extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String password;

    @Column(length = 100)
    private String status;

    @Column(name = "user_role", length = 100)
    private String userRole;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Volunteer volunteer;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private MilitaryPersonnel militaryPersonnel;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Request> requests = new ArrayList<>();
}
