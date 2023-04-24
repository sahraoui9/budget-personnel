package bgpersonnel.budget.category;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public class CategoryService {
    private final CategoryRepository  categoryRepository;
    
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Retourne la liste des catégories.
     * @return la liste de toutes les catégories.
     */
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    /**
     * Sauvegarde une nouvelle catégorie dans la base de données
     * @param category à sauvegarder
     * @return catégorie sauvegarder avec son id.
     */
    public Category create(Category category) {
        return categoryRepository.save(category);
    }

    /**
     * Remplace les données d'une catégorie dans la base de données en fonction de son id.
     * @param category nouvelles données à sauvegarder
     * @return la catégorie avec les nouvelles données.
     */
    public Category update(Category category){
        return this.categoryRepository.save(category);
    }


    /**
     * Recherche une catégorie en fonction de son id.
     * @param id de la catégorie à rechercher
     * @return la catégorie portant l'id passé en paramètre
     * @throws ResponseStatusException si aucune catégorie ne porte cet id
     */
    public Category findById(Long id) {
        return categoryRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    

    /**
     * supprime une catégorie en fonction de son id.
     * @param id de la catégorie à supprimer.
     */
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
}
