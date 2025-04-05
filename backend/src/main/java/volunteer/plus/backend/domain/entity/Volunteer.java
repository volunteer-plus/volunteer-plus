package volunteer.plus.backend.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true, exclude = {"user", "levies", "volunteerFeedbacks"})
@Entity
@Table(name = "volunteer")
public class Volunteer extends BaseEntity {

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

    @OneToMany(mappedBy = "volunteer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VolunteerFeedback> volunteerFeedbacks = new ArrayList<>();

    public void addFeedback(VolunteerFeedback volunteerFeedback) {
        if (this.volunteerFeedbacks == null) {
            this.volunteerFeedbacks = new ArrayList<>();
        }
        this.volunteerFeedbacks.add(volunteerFeedback);
        volunteerFeedback.setVolunteer(this);
    }

    public void removeFeedback(VolunteerFeedback volunteerFeedback) {
        this.volunteerFeedbacks.remove(volunteerFeedback);
        volunteerFeedback.setVolunteer(null);
    }

    public BigDecimal getReputationScore() {
        if (this.volunteerFeedbacks == null || this.volunteerFeedbacks.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return this.volunteerFeedbacks
                .stream()
                .map(VolunteerFeedback::getReputationScore)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(this.volunteerFeedbacks.size()), 8, RoundingMode.HALF_UP);
    }
}
