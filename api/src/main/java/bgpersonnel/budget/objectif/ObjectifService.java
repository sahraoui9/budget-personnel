package bgpersonnel.budget.objectif;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;


@Service
public class ObjectifService {

    @Autowired
    private ObjectifRepository objectifRepository;

    public ObjectifService(ObjectifRepository objectifRepository) {
        this.objectifRepository = objectifRepository;
    }

    // CRUD operations

    public Objectif createObjectif(Objectif objectif) {
        return objectifRepository.save(objectif);
    }

    public Objectif findById(Long id) {
        return objectifRepository.findById(id).get();
    }

    public Iterable<Objectif> findAll() {
        return objectifRepository.findAll();
    }

    public Objectif update(Objectif objectif) {
        return objectifRepository.save(objectif);
    }

    public void deleteById(Long id) {
        objectifRepository.deleteById(id);
    }

    public List<Objectif> findByUser(Long id) {
        return objectifRepository.findByUser(id);
    }

}
