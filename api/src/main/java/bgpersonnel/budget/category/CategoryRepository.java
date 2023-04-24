package bgpersonnel.budget.category;

import bgpersonnel.budget.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
