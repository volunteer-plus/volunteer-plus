package volunteer.plus.backend.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true, exclude = {"militaryPersonnel", "user", "levies"})
@Entity
@Table(name = "request")
public class Request extends BaseEntity {

    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String description;

    private LocalDateTime deadline;

    private BigDecimal amount;

    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL)
    private List<Levy> levies = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "military_personnel_id")
    private MilitaryPersonnel militaryPersonnel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
