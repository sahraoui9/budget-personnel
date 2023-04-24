package bgpersonnel.budget.transaction;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @PostMapping("/user/{userId}")
    public Transaction create(@RequestBody Transaction transaction, @PathVariable long userId) {
        return service.create(transaction, userId);
    }

    @PutMapping("{id}")
    public Transaction update(@RequestBody Transaction transaction) {
        return service.update(transaction);
    }

    @DeleteMapping("{id}")
    public void deleteById(@PathVariable Long id) {
        service.deleteById(id);
    }


    @GetMapping
    public List<Transaction> findAll() {
        return service.findAll();
    }

    @GetMapping("{id}")
    public Transaction findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping("/category/{id}")
    public List<Transaction> findByCategory(@PathVariable Long id) {
        return service.findByCategory(id);
    }

    @GetMapping("/date/{id}")
    public List<Transaction> findByDate(@PathVariable String date) {
        return service.findByDate(date);
    }

}
