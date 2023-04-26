package bgpersonnel.budget.budget;


import bgpersonnel.budget.objectif.Objectif;
import bgpersonnel.budget.objectif.ObjectifService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
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


    @GetMapping("/annual-global/{userId}/{year}")
    public ResponseEntity<Double> calculateAnnualGlobalBudgetForYear(@PathVariable("userId") Long userId,  @PathVariable("year") Integer year) {
        double result = budgetService.calculateAnnualGlobalBudgetForYear(userId, year);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/annual-global/{userId}")
    public ResponseEntity<List<Map<String, Object>>> getTotalAnnualBudgetsByYearAndUser(@PathVariable Long userId) {
        List<Map<String, Object>> budgetList = budgetService.getTotalAnnualBudgetsByYearAndUser(userId);
        return ResponseEntity.ok(budgetList);
    }

    @GetMapping("/monthly-global/{userId}/{month}/{year}")
    public ResponseEntity<Double> calculateMonthlyGlobalBudgetForYearAndMonth(@PathVariable Long userId, @PathVariable Integer month, @PathVariable Integer year) {
        Double budget = budgetService.calculateMonthlyGlobalBudgetForYearAndMonth(userId, month, year);
        return ResponseEntity.ok(budget);
    }

    @GetMapping("/monthly-global/{id}/{year}")
    public ResponseEntity<List<Map<String, Object>>> getMonthlyGlobalBudgetForYear(@PathVariable("id") Long id,
                                                                                   @PathVariable("year") Integer year) {
        List<Map<String, Object>> budgetList = budgetService.calculateMonthlyGlobalBudgetForYear(id, year);
        return ResponseEntity.ok().body(budgetList);
    }


    @GetMapping("/monthly-category/{year}/{userId}/{budgetId}")
    public ResponseEntity<List<Map<String, Object>>> calculateMonthlyBudgetForYearAndCategory(@PathVariable Integer year, @PathVariable Long userId, @PathVariable Long budgetId) {
        List<Map<String, Object>> result = budgetService.calculateMonthlyBudgetForYearAndCategory(year, userId, budgetId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/monthly-category/{userId}/monthly/{year}/{month}/{budgetId}")
    public ResponseEntity<Double> getMonthlyBudgetForYearAndMonthAndCategory(@PathVariable Long userId,
                                                                                   @PathVariable Integer month,
                                                                                   @PathVariable Integer year,
                                                                                   @PathVariable Long budgetId) {
        Double monthlyGlobalBudget = budgetService.calculateMonthlyBudgetForYearAndMonthAndCategory(userId, month, year, budgetId);
        return ResponseEntity.ok(monthlyGlobalBudget);
    }

    @GetMapping("/annual-category/{userId}/{budgetId}")
    public ResponseEntity<List<Map<String, Object>>> getTotalAnnualBudgetsByYearAndUserAndCategory(@PathVariable Long userId, @RequestParam Long budgetId) {
        List<Map<String, Object>> result = budgetService.getTotalAnnualBudgetsByYearAndUserAndCategory(userId, budgetId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/annual-category/{year}/{userId}/{budgetId}")
    public ResponseEntity<Double> calculateAnnualBudgetForYearAndCategory(
            @PathVariable Long userId,
            @PathVariable Long budgetId,
            @PathVariable Integer year) {

        Double annualGlobalBudget = budgetService.calculateAnnualBudgetForYearAndCategory(year, userId, budgetId);

        if (annualGlobalBudget == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(annualGlobalBudget);
    }





}
