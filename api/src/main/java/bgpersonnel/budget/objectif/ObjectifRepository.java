package bgpersonnel.budget.objectif;

import bgpersonnel.budget.objectif.Objectif;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ObjectifRepository extends JpaRepository<Objectif, Long>{
    Objectif findByName(String name);

    List<Objectif> findByUser(Long id);

}
