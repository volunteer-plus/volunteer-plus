package volunteer.plus.backend.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true, exclude = "report")
@Entity
@Table(name = "attachment")
public class Attachment extends BaseEntity {

    @Column(columnDefinition = "LONGTEXT")
    private String filepath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id")
    private Report report;
}
