package bgpersonnel.budget.budget;


import bgpersonnel.budget.objectif.Objectif;
import bgpersonnel.budget.objectif.ObjectifRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;


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


}
