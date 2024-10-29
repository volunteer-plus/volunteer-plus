package volunteer.plus.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import volunteer.plus.backend.domain.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String userEmail);

    List<UserMainDataProjection> findAllByEmailNotNull();

    interface UserMainDataProjection {
        String getFirstName();
        String getLastName();
        String getEmail();
    }
}
