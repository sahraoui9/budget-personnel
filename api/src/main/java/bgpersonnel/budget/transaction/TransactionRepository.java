package bgpersonnel.budget.transaction;

import bgpersonnel.budget.authentification.common.entity.User;
import bgpersonnel.budget.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUserAndDateTransactionBetween(User user, LocalDateTime dateDebut, LocalDateTime dateFin);

    List<Transaction> findByCategoryAndUser(Category category, User user);

    List<Transaction> findByCategoryAndDateTransactionBetween(Category category, LocalDateTime dateDebut, LocalDateTime dateFin);

    List<Transaction> findByDateTransactionAndUser(LocalDateTime localDateTime, User user);
}
