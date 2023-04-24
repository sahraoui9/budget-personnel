package bgpersonnel.budget.authentification.repository;


import bgpersonnel.budget.authentification.entity.ERole;
import bgpersonnel.budget.authentification.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Integer> {
  Optional<Role> findByName(ERole name);
}
