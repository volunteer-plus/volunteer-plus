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
@EqualsAndHashCode(callSuper = false, exclude = "militaryPersonnel")
@Entity
@Table(name = "brigade")
public class Brigade extends BaseEntity {

    @Column(name = "regiment_code", nullable = false, unique = true)
    private String regimentCode;

    @Column
    private String branch;

    @Column
    private String role;

    @Column
    private String partOf;

    @Column(name = "website_link")
    private String websiteLink;

    @Column(name = "current_commander")
    private String currentCommander;

    @Column(columnDefinition = "LONGTEXT")
    private String description;

    @OneToMany(mappedBy = "brigade", cascade = CascadeType.ALL)
    private List<MilitaryPersonnel> militaryPersonnel = new ArrayList<>();

    public void setupMilitaryPersonnel(List<MilitaryPersonnel> mps) {
        if (mps == null) {
            return;
        }
        if (this.militaryPersonnel == null || this.militaryPersonnel.isEmpty()) {
            this.militaryPersonnel = mps;
        } else {
            this.militaryPersonnel.clear();
            this.militaryPersonnel.addAll(mps);
        }
        mps.forEach(mp -> mp.setBrigade(this));
    }

    public void removeMilitaryPersonnel(MilitaryPersonnel mp) {
        if (mp != null && this.militaryPersonnel != null && !this.militaryPersonnel.isEmpty()) {
            this.militaryPersonnel.remove(mp);
            mp.setBrigade(null);
        }
    }
}
