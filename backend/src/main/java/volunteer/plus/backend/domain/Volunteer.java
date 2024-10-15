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
@EqualsAndHashCode(callSuper = true, exclude = {"user", "levies"})
@Entity
@Table(name = "volunteer")
public class Volunteer extends BaseEntity {

    @Column(name = "reputation_score", nullable = false)
    private BigDecimal reputationScore;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany
    @JoinTable(
            name = "volunteer_levy",
            joinColumns = @JoinColumn(name = "volunteer_id"),
            inverseJoinColumns = @JoinColumn(name = "levy_id")
    )
    private List<Levy> levies = new ArrayList<>();
}
