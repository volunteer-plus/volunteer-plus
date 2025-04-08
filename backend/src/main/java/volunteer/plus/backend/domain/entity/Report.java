package volunteer.plus.backend.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false, exclude = {"levy", "attachments"})
@Entity
@Table(name = "report")
public class Report extends BaseEntity {

    @Column(columnDefinition = "LONGTEXT")
    private String data;

    @Column(name = "is_analyzed")
    private boolean analyzed;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "levy_id")
    private Levy levy;

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL)
    private List<Attachment> attachments = new ArrayList<>();

    public void addAttachment(Attachment attachment) {
        if (this.attachments == null) {
            this.attachments = new ArrayList<>();
        }
        this.attachments.add(attachment);
        attachment.setReport(this);
    }

    public void removeAttachment(Attachment attachment) {
        if (this.attachments != null && !this.attachments.isEmpty()) {
            this.attachments.remove(attachment);
            attachment.setReport(null);
        }
    }
}
