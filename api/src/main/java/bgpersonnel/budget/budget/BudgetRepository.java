package bgpersonnel.budget.budget;

import bgpersonnel.budget.objectif.Objectif;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long>{
    Objectif findByName(String name);
    List<Objectif> findByUser(Long id);

    @Query(value = "SELECT SUM(CASE WHEN t.typeTransaction = 'REVENUE' THEN t.amount ELSE 0 END) - SUM(CASE WHEN t.typeTransaction = 'EXPENSE' OR t.typeTransaction = 'SAVING' THEN t.amount ELSE 0 END) "
            + "FROM Transaction t "
            + "WHERE YEAR(t.dateTransaction) = :year "
            + "AND t.user.id = :userId")
    Double calculateAnnualGlobalBudgetForYear(Integer year, Long userId);



    @Query(value = "SELECT YEAR(t.dateTransaction) as year, SUM(CASE WHEN t.typeTransaction = 'REVENUE' THEN t.amount ELSE 0 END) - SUM(CASE WHEN t.typeTransaction IN ('EXPENSE', 'SAVING') THEN t.amount ELSE 0 END) as totalBudget "
            + "FROM Transaction t "
            + "WHERE t.user.id = :userId "
            + "GROUP BY YEAR(t.dateTransaction)")
    List<Map<String, Object>> getTotalAnnualBudgetsByYearAndUser(@Param("userId") Long userId);


    @Query("SELECT MONTH(t.dateTransaction), YEAR(t.dateTransaction) as year, SUM(CASE WHEN t.typeTransaction = 'REVENUE' THEN t.amount ELSE 0 END) - SUM(CASE WHEN t.typeTransaction = 'EXPENSE' OR t.typeTransaction = 'SAVINGS' THEN t.amount ELSE 0 END)"
            + " FROM Transaction t WHERE YEAR(t.dateTransaction) = :year AND t.user.id = :userId GROUP BY MONTH(t.dateTransaction)")
    List<Map<String, Object>> calculateMonthlyGlobalBudgetForYear(int year, long userId);


    @Query(value = "SELECT COALESCE(SUM(CASE WHEN t.typeTransaction = 'REVENUE' THEN t.amount ELSE 0 END), 0) - COALESCE(SUM(CASE WHEN t.typeTransaction = 'EXPENSE' OR t.typeTransaction = 'SAVING' THEN t.amount ELSE 0 END), 0) " +
            "FROM Transaction t " +
            "WHERE t.user.id = :userId AND MONTH(t.dateTransaction) = :month AND YEAR(t.dateTransaction) = :year")
    Double calculateMonthlyGlobalBudgetForYearAndMonth(Long userId, Integer month, Integer year);

}
