package volunteer.plus.backend.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import volunteer.plus.backend.config.security.EmailAttributeConverter;
import volunteer.plus.backend.service.general.impl.CryptoConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
public class User implements UserDetails, OidcUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(nullable = false, unique = true)
    @Convert(converter = EmailAttributeConverter.class)
    private String email;

    @Column
    private String password;

    @Column(length = 100)
    private String status;

    @Column(name = "first_name")
    @Convert(converter = CryptoConverter.class)
    private String firstName;

    @Column(name = "middle_name")
    @Convert(converter = CryptoConverter.class)
    private String middleName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "last_name")
    @Convert(converter = CryptoConverter.class)
    private String lastName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "logo_s3_link")
    private String logoS3Link;

    @Column(name = "logo_filename")
    private String logoFilename;

    @Column(name = "email_hash", nullable = false, unique = true, updatable = false)
    private String emailHash;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Volunteer volunteer;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private MilitaryPersonnel militaryPersonnel;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Request> requests = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<VolunteerFeedback> volunteerFeedbacks = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<PaymentOrder> paymentOrders = new ArrayList<>();

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

    @Lob
    @Column(nullable = false)
    private byte[] centroid;

    @Column(nullable = false)
    private double threshold;

    @Lob
    @Column(nullable = false)
    private byte[] embeddings;

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

    @CreationTimestamp
    protected LocalDateTime createDate;

    @UpdateTimestamp
    protected LocalDateTime updateDate;

    @PrePersist
    protected void onCreate() {
        this.createDate = LocalDateTime.now();

        if (email != null) {
            this.emailHash = DigestUtils.sha256Hex(email.trim().toLowerCase());
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateDate = LocalDateTime.now();

        if (email != null) {
            this.emailHash = DigestUtils.sha256Hex(email.trim().toLowerCase());
        }
    }

    public void setUpdateDate() {
        this.updateDate = LocalDateTime.now();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of("user", email);
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

    @Override
    public String getName() {
        return email;
    }

    @Override
    public Map<String, Object> getClaims() {
        return Map.of();
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return null;
    }

    @Override
    public OidcIdToken getIdToken() {
        return null;
    }
}
