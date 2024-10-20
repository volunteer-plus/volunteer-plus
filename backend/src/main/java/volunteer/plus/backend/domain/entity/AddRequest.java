package volunteer.plus.backend.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "add_request")
public class AddRequest extends BaseEntity {
    @ToString.Exclude
    @Column(name = "request_id")
    private String requestId;

    @Column(name = "executed")
    private boolean executed;
}
