package bgpersonnel.budget.objectif;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/objectifs")
public class ObjectifController {

    @Autowired
    private ObjectifService objectifService;

    public ObjectifController(ObjectifService objectifService) {
        this.objectifService = objectifService;
    }

    @GetMapping("{id}")
    public Optional<Objectif> findById(Long id) {
        return Optional.ofNullable(objectifService.findById(id));
    }

    @GetMapping("/")
    public Iterable<Objectif> findAll() {
        return objectifService.findAll();
    }

    @PostMapping("/")
    public Objectif create(Objectif objectif) {
        return objectifService.createObjectif(objectif);
    }

    @DeleteMapping("{id}")
    public void deleteById(@PathVariable Long id) {
        objectifService.deleteById(id);
    }

    @PutMapping
    public Objectif update(@RequestBody Objectif objectif) {
        return objectifService.update(objectif);
    }

    @GetMapping("/user/{id}")
    public Iterable<Objectif> findByUser(@PathVariable Long id) {
        return objectifService.findByUser(id);
    }



}
