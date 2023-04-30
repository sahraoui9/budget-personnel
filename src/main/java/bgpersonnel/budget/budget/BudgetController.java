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


    @GetMapping("/annual-global/{year}")
    public ResponseEntity<Double> calculateAnnualGlobalBudgetForYear(  @PathVariable("year") Integer year) {
        double result = budgetService.calculateAnnualGlobalBudgetForYear( year);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/annual-global/")
    public ResponseEntity<List<Map<String, Object>>> getTotalAnnualBudgetsByYearAndUser() {
        List<Map<String, Object>> budgetList = budgetService.getTotalAnnualBudgetsByYearAndUser();
        return ResponseEntity.ok(budgetList);
    }

    @GetMapping("/monthly-global/{month}/{year}")
    public ResponseEntity<Double> calculateMonthlyGlobalBudgetForYearAndMonth( @PathVariable Integer month, @PathVariable Integer year) {
        Double budget = budgetService.calculateMonthlyGlobalBudgetForYearAndMonth( month, year);
        return ResponseEntity.ok(budget);
    }

    @GetMapping("/monthly-global/{year}")
    public ResponseEntity<List<Map<String, Object>>> getMonthlyGlobalBudgetForYear(@PathVariable("year") Integer year) {
        List<Map<String, Object>> budgetList = budgetService.calculateMonthlyGlobalBudgetForYear( year);
        return ResponseEntity.ok().body(budgetList);
    }


    @GetMapping("/monthly-category/{year}/{budgetId}")
    public ResponseEntity<List<Map<String, Object>>> calculateMonthlyBudgetForYearAndCategory(@PathVariable Integer year, @PathVariable Long budgetId) {
        List<Map<String, Object>> result = budgetService.calculateMonthlyBudgetForYearAndCategory(year, budgetId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/monthly-category/monthly/{year}/{month}/{budgetId}")
    public ResponseEntity<Double> getMonthlyBudgetForYearAndMonthAndCategory(@PathVariable Integer month,
                                                                                   @PathVariable Integer year,
                                                                                   @PathVariable Long budgetId) {
        Double monthlyGlobalBudget = budgetService.calculateMonthlyBudgetForYearAndMonthAndCategory( month, year, budgetId);
        return ResponseEntity.ok(monthlyGlobalBudget);
    }

    @GetMapping("/annual-category/{budgetId}")
    public ResponseEntity<List<Map<String, Object>>> getTotalAnnualBudgetsByYearAndUserAndCategory(@RequestParam Long budgetId) {
        List<Map<String, Object>> result = budgetService.getTotalAnnualBudgetsByYearAndUserAndCategory( budgetId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/annual-category/{year}/{budgetId}")
    public ResponseEntity<Double> calculateAnnualBudgetForYearAndCategory(@PathVariable Long budgetId,
            @PathVariable Integer year) {

        Double annualGlobalBudget = budgetService.calculateAnnualBudgetForYearAndCategory(year, budgetId);

        if (annualGlobalBudget == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(annualGlobalBudget);
    }


    @GetMapping("/adjustement/")
    public ResponseEntity<List<Map<String, Object>>> getAdjustement() {
        List<Map<String, Object>> result = budgetService.calculateAdjustments();
        return ResponseEntity.ok(result);
    }





}
