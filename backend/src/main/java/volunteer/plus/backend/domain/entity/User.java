package volunteer.plus.backend.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.*;

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

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "logo_s3_link")
    private String logoS3Link;

    @Column(name = "logo_filename")
    private String logoFilename;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Volunteer volunteer;

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private VerificationToken verificationToken;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PasswordResetToken passwordResetToken;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private User2fa user2fa;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TrustedDevice> trustedDevices = new ArrayList<>();

    private boolean enabled;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean isTwoFactorEnabled;

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
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
            for (Privilege privilege : role.getPrivileges()) {
                authorities.add(new SimpleGrantedAuthority(privilege.getName()));
            }
        }
        return authorities;
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
