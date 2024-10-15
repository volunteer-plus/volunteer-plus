package volunteer.plus.backend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true, exclude = {"levy", "attachments"})
@Entity
@Table(name = "report")
public class Report extends BaseEntity {

    @Column(columnDefinition = "LONGTEXT")
    private String data;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "levy_id")
    private Levy levy;

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL)
    private List<Attachment> attachments = new ArrayList<>();
}
