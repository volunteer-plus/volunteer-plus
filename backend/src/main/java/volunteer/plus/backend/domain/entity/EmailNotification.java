package volunteer.plus.backend.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import volunteer.plus.backend.domain.enums.EmailStatus;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false, exclude = {"emailRecipients", "emailAttachments", "template"})
@Entity
@Table(name = "email_notification")
public class EmailNotification extends BaseEntity {
    @Column(name = "subject_data")
    private String subjectData;

    @Column(name = "template_data")
    private String templateData;

    @Column(name = "is_deleted")
    private boolean deleted;

    @Column(name = "draft")
    private boolean draft;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EmailStatus status;

    @ManyToOne
    @JoinColumn(name = "email_template_id")
    private EmailTemplate template;

    @OneToMany(mappedBy = "emailNotification", cascade = CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
    private List<EmailRecipient> emailRecipients = new ArrayList<>();

    @OneToMany(mappedBy = "emailNotification", cascade = CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
    private List<EmailAttachment> emailAttachments = new ArrayList<>();
}
