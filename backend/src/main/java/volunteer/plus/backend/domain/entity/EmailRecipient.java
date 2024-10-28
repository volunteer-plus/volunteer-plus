package volunteer.plus.backend.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false, exclude = {"emailNotification"})
@Entity
@Table(name = "email_recipient")
public class EmailRecipient extends BaseEntity {
    @Column(name = "email_address")
    private String emailAddress;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "sent_date")
    private LocalDateTime sentDate;

    @Column(name = "to_recipient")
    private boolean toRecipient;

    @Column(name = "cc")
    private boolean ccRecipient;

    @Column(name = "bcc")
    private boolean bccRecipient;

    @Column(name = "sent")
    private boolean sent;

    @ManyToOne
    @JoinColumn(name = "email_notification_id")
    private EmailNotification emailNotification;
}
