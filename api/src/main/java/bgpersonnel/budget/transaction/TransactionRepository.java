package bgpersonnel.budget.transaction;

import bgpersonnel.budget.authentification.common.entity.User;
import bgpersonnel.budget.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByCategoryAndUser(Category category, User user);

    List<Transaction> findByCategoryAndDateTransactionBetween(Category category, LocalDateTime dateDebut, LocalDateTime dateFin);

    List<Transaction> findByDateTransactionAndUser(LocalDateTime localDateTime, User user);
    List<Transaction> findByDateTransactionAndUser(LocalDateTime localDateTime, Long userId);

    // find all transactions by user between two dates and by category and category is optional
    @Query("SELECT t FROM Transaction t WHERE t.user.id = ?1 AND t.dateTransaction BETWEEN ?2 AND ?3 AND (t.category.id = ?4 OR ?4 IS NULL)")
    List<Transaction> findAllByUserBetweenTwoDatesAndByCategory(Long userId, LocalDate date1, LocalDate date2, Long categoryId);
}
