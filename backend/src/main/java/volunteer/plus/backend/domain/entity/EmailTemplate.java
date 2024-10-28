package volunteer.plus.backend.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false, exclude = {"emailNotifications"})
@Entity
@Table(name = "email_template")
public class EmailTemplate extends BaseEntity {
    @Column(name = "email_message_tag")
    private String emailMessageTag;

    @Column(name = "subject")
    private String subject;

    @Column(name = "body")
    private String body;

    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
    private List<EmailNotification> emailNotifications = new ArrayList<>();
}
