package volunteer.plus.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import volunteer.plus.backend.domain.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
