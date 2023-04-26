package bgpersonnel.budget.transaction;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByCategoryAndUser(long id, Long userId);

    List<Transaction> findByDateTransactionAndUser(LocalDateTime localDateTime, Long userId);
}
