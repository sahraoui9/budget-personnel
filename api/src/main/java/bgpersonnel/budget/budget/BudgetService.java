package bgpersonnel.budget.budget;


import bgpersonnel.budget.objectif.Objectif;
import bgpersonnel.budget.objectif.ObjectifRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;


    public BudgetService(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    // CRUD operations

    public Budget create(Budget budget) {
        return budgetRepository.save(budget);
    }

    public Budget findById(Long id) {
        return budgetRepository.findById(id).get();
    }

    public Iterable<Budget> findAll() {
        return budgetRepository.findAll();
    }

    public Budget update(Budget budget) {
        return budgetRepository.save(budget);
    }

    public void deleteById(Long id) {
        budgetRepository.deleteById(id);
    }

    public List<Budget> findByUser(Long id) {
        return budgetRepository.findByUser(id);
    }

    public double calculateAnnualGlobalBudgetForYear(Long id, Integer year) {
        return budgetRepository.calculateAnnualGlobalBudgetForYear(year,id );
    }

    public List<Map<String, Object>> getTotalAnnualBudgetsByYearAndUser(Long id) {
        return budgetRepository.getTotalAnnualBudgetsByYearAndUser(id );
    }
    public Double calculateMonthlyGlobalBudgetForYearAndMonth(Long userId, Integer month, Integer year) {
        return budgetRepository.calculateMonthlyGlobalBudgetForYearAndMonth(userId, month, year);
    }

    public List<Map<String, Object>> calculateMonthlyGlobalBudgetForYear(Long id, Integer year) {
        return budgetRepository.calculateMonthlyGlobalBudgetForYear(year,id );
    }

    public List<Map<String, Object>> calculateMonthlyBudgetForYearAndCategory(Integer year, Long userId, Long budgetId) {
        return budgetRepository.calculateMonthlyBudgetForYearAndCategory(year, userId, budgetId);
    }

    public Double calculateMonthlyBudgetForYearAndMonthAndCategory(Long userId, Integer month, Integer year, Long budgetId) {
        return budgetRepository.calculateMonthlyBudgetForYearAndMonthAndCategory(userId, month, year, budgetId);
    }

    public List<Map<String, Object>> getTotalAnnualBudgetsByYearAndUserAndCategory(Long userId, Long budgetId) {
        return budgetRepository.getTotalAnnualBudgetsByYearAndUserAndCategory(userId, budgetId);
    }

    public Double calculateAnnualBudgetForYearAndCategory(Integer year, Long userId, Long budgetId) {
        return budgetRepository.calculateAnnualBudgetForYearAndCategory(year, userId, budgetId);
    }



}
