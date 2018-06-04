package pl.dmcs.mww.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.dmcs.mww.model.Role;
import pl.dmcs.mww.model.RoleName;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleName);
}