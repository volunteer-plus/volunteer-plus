package volunteer.plus.backend.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false, exclude = {
        "volunteer",
        "requests",
        "volunteerFeedbacks",
        "liqPayOrders",
        "militaryPersonnel",
        "conversationRooms"
})
@Entity
@Table(name = "user")
public class User extends BaseEntity implements UserDetails {

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

    @Column(name = "reset_token")
    private String resetToken;

    @Column(name = "reset_token_expiration_time")
    private Long resetTokenExpTime;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private MilitaryPersonnel militaryPersonnel;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Request> requests = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<VolunteerFeedback> volunteerFeedbacks = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<LiqPayOrder> liqPayOrders = new ArrayList<>();

    @ManyToMany(mappedBy = "users")
    private List<ConversationRoom> conversationRooms = new ArrayList<>();

    private boolean enabled;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;

    public void addFeedback(VolunteerFeedback volunteerFeedback) {
        if (this.volunteerFeedbacks == null) {
            this.volunteerFeedbacks = new ArrayList<>();
        }
        this.volunteerFeedbacks.add(volunteerFeedback);
        volunteerFeedback.setUser(this);
    }

    public void addRequest(Request request) {
        if (this.requests == null) {
            this.requests = new ArrayList<>();
        }
        this.requests.add(request);
        request.setUser(this);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
