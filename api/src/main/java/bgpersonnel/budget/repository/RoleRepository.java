package bgpersonnel.budget.repository;


import bgpersonnel.budget.entity.ERole;
import bgpersonnel.budget.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Integer> {
  Optional<Role> findByName(ERole name);
}
