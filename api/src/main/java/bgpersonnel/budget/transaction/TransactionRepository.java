package bgpersonnel.budget.transaction;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByCategory(long id);

    List<Transaction> findByDateTransaction(LocalDateTime localDateTime);
}
