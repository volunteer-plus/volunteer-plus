package volunteer.plus.backend.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import volunteer.plus.backend.domain.dto.RoleDto;
import volunteer.plus.backend.domain.dto.RoleWithSubrolesDto;
import volunteer.plus.backend.service.general.RoleService;

import java.util.List;

@RestController
@RequestMapping("/api/admin/roles")
@PreAuthorize("hasAuthority('CAN_MANAGE_ROLES')")
@RequiredArgsConstructor
public class AdminRoleController {

    private final RoleService roleService;

    @GetMapping
    public List<RoleDto> listRoles() {
        return roleService.getAllRoles();
    }

    @GetMapping("/{id}/subroles")
    public RoleWithSubrolesDto listSubRoles(@PathVariable Long id) {
        return roleService.getSubRoles(id);
    }

    @PostMapping("/assign")
    public ResponseEntity<Void> assign(@RequestParam Long userId,
                                       @RequestParam Long roleId) {
        roleService.assignRoleToUser(userId, roleId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/remove")
    public ResponseEntity<Void> remove(@RequestParam Long userId,
                                       @RequestParam Long roleId) {
        roleService.removeRoleFromUser(userId, roleId);
        return ResponseEntity.ok().build();
    }

}
