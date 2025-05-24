package volunteer.plus.backend.service.general.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import volunteer.plus.backend.domain.dto.RoleDto;
import volunteer.plus.backend.domain.dto.RoleWithSubrolesDto;
import volunteer.plus.backend.domain.entity.Role;
import volunteer.plus.backend.domain.entity.User;
import volunteer.plus.backend.repository.RoleRepository;
import volunteer.plus.backend.repository.UserRepository;
import volunteer.plus.backend.service.general.RoleService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepo;
    private final UserRepository userRepo;


    @Override
    public List<RoleDto> getAllRoles() {
        return roleRepo.findAll().stream()
                .map(r -> new RoleDto(r.getId(), r.getName()))
                .toList();
    }

    @Override
    public RoleWithSubrolesDto getSubRoles(Long roleId) {
        Role role = roleRepo.findById(roleId)
                .orElseThrow(() -> new EntityNotFoundException("Роль не знайдена"));
        Set<RoleDto> subs = role.getPrivileges().stream()
                .map(sr -> new RoleDto(sr.getId(), sr.getName()))
                .collect(Collectors.toSet());
        return new RoleWithSubrolesDto(role.getId(), role.getName(), subs);
    }

    @Override
    @Transactional
    public void assignRoleToUser(Long userId, Long roleId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Користувач не знайдений"));
        Role role = roleRepo.findById(roleId)
                .orElseThrow(() -> new EntityNotFoundException("Роль не знайдена"));
        user.getRoles().add(role);
        userRepo.save(user);
    }

    @Override
    @Transactional
    public void removeRoleFromUser(Long userId, Long roleId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Користувач не знайдений"));
        Role role = roleRepo.findById(roleId)
                .orElseThrow(() -> new EntityNotFoundException("Роль не знайдена"));
        user.getRoles().remove(role);
        userRepo.save(user);
    }
}
