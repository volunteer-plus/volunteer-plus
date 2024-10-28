package volunteer.plus.backend.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false, exclude = {"emailNotification"})
@Entity
@Table(name = "email_attachment")
public class EmailAttachment extends BaseEntity {
    @Column(name = "filename")
    private String filename;

    @Column(name = "s3_link")
    private String s3Link;

    @ManyToOne
    @JoinColumn(name = "email_notification_id")
    private EmailNotification emailNotification;
}
