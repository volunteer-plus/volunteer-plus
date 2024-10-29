package volunteer.plus.backend.domain.entity;

import jakarta.persistence.*;
import lombok.*;
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

    @OneToMany(mappedBy = "emailNotification", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<EmailRecipient> emailRecipients = new ArrayList<>();

    @OneToMany(mappedBy = "emailNotification", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<EmailAttachment> emailAttachments = new ArrayList<>();

    public void addRecipient(EmailRecipient emailRecipient) {
        if (this.emailRecipients == null) {
            this.emailRecipients = new ArrayList<>();
        }
        this.emailRecipients.add(emailRecipient);
        emailRecipient.setEmailNotification(this);
    }

    public void addAttachment(EmailAttachment emailAttachment) {
        if (this.emailAttachments == null) {
            this.emailAttachments = new ArrayList<>();
        }
        this.emailAttachments.add(emailAttachment);
        emailAttachment.setEmailNotification(this);
    }
}
