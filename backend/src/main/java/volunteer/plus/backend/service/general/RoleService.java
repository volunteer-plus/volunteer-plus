package volunteer.plus.backend.service.general;

import volunteer.plus.backend.domain.dto.RoleDto;
import volunteer.plus.backend.domain.dto.RoleWithSubrolesDto;

import java.util.List;

public interface RoleService {

    List<RoleDto> getAllRoles();
    RoleWithSubrolesDto getSubRoles(Long roleId);
    void assignRoleToUser(Long userId, Long roleId);
    void removeRoleFromUser(Long userId, Long roleId);
}
