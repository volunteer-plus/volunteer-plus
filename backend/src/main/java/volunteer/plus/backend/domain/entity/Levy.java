package volunteer.plus.backend.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import volunteer.plus.backend.domain.enums.LevyStatus;

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

    @Column(name = "accumulated", nullable = false)
    private BigDecimal accumulated;

    @Column(name = "goal_amount", nullable = false)
    private BigDecimal goalAmount;

    @Column(name = "category")
    private String category;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private LevyStatus status;

    @Column(name = "description", columnDefinition = "LONGTEXT")
    private String description;

    @Column(name = "trophy_description", columnDefinition = "LONGTEXT")
    private String trophyDescription;

    @ManyToMany(mappedBy = "levies")
    private List<Volunteer> volunteers = new ArrayList<>();

    @OneToOne(mappedBy = "levy", cascade = CascadeType.ALL)
    private Report report;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id")
    private Request request;

    public void addVolunteer(Volunteer volunteer) {
        if (this.volunteers == null) {
            this.volunteers = new ArrayList<>();
        }
        this.volunteers.add(volunteer);
        volunteer.getLevies().add(this);
    }
}
