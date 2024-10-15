package volunteer.plus.backend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true, exclude = {"request", "volunteers", "report"})
@Entity
@Table(name = "levy")
public class Levy extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id")
    private Request request;

    @Column(nullable = false)
    private BigDecimal accumulated;

    @Column(name = "trophy_description", columnDefinition = "LONGTEXT")
    private String trophyDescription;

    @ManyToMany(mappedBy = "levies")
    private List<Volunteer> volunteers = new ArrayList<>();

    @OneToOne(mappedBy = "levy", cascade = CascadeType.ALL)
    private Report report;
}
