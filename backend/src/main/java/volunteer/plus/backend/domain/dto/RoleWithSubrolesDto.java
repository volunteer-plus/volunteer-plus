package volunteer.plus.backend.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleWithSubrolesDto {

    private Long id;
    private String name;
    private Set<RoleDto> subRoles;
}
