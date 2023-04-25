package bgpersonnel.budget.budget;


import bgpersonnel.budget.objectif.Objectif;
import bgpersonnel.budget.objectif.ObjectifService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/budgets")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }


    @GetMapping("{id}")
    public Optional<Budget> findById(Long id) {
        return Optional.ofNullable(budgetService.findById(id));
    }

    @GetMapping("/")
    public Iterable<Budget> findAll() {
        return budgetService.findAll();
    }

    @PostMapping("/")
    public Budget create(Budget budget) {
        return budgetService.create(budget);
    }

    @DeleteMapping("{id}")
    public void deleteById(@PathVariable Long id) {
        budgetService.deleteById(id);
    }

    @PutMapping
    public Budget update(@RequestBody Budget budget) {
        return budgetService.update(budget);
    }


}
