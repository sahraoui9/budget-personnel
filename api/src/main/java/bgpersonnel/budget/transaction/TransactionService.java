package bgpersonnel.budget.transaction;

import bgpersonnel.budget.authentification.common.entity.User;
import bgpersonnel.budget.authentification.common.services.UserService;
import bgpersonnel.budget.category.Category;
import bgpersonnel.budget.category.CategoryRepository;
import bgpersonnel.budget.category.CategoryService;
import bgpersonnel.budget.objectif.Objectif;
import bgpersonnel.budget.objectif.ObjectifService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserService userService;
    private final CategoryService categoryService;

    @Autowired
    private ObjectifService objectifService;

    public TransactionService(
            TransactionRepository transactionRepository,
            UserService userService,
            CategoryService categoryService
    ) {
        this.transactionRepository = transactionRepository;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    /**
     * Retourne la liste des transactions.
     * @return la liste de toutes les transactions.
     */
    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    /**
     * Sauvegarde une nouvelle transaction dans la base de données en fonction du user
     * @param transaction à sauvegarder
     * @param userId id de l'utilisateur qui fait la transaction
     * @return transaction sauvegarder avec son id.
     */
    public Transaction create(Transaction transaction, long userId) {
        User user = this.userService.findById(userId);
        transaction.setUser(user);
        Transaction savedTransaction = transactionRepository.save(transaction);
        Objectif objectif = transaction.getObjectif();
        if (objectif != null) {
            boolean objectifAtteint = objectifService.isObjectifAtteint(objectif.getId());
        }

        return savedTransaction;

    }

    /**
     * Remplace les données d'une transaction dans la base de données en fonction de son id.
     * @param transaction nouvelles données à sauvegarder
     * @return la transaction avec les nouvelles données.
     */
    public Transaction update(Transaction transaction){
        return this.transactionRepository.save(transaction);
    }


    /**
     * Recherche une transaction en fonction de son id.
     * @param id de la transaction à rechercher
     * @return la transaction portant l'id passé en paramètre
     * @throws org.springframework.web.server.ResponseStatusException si aucune transaction ne porte cet id
     */
    public Transaction findById(Long id) {
        return transactionRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
    }


    /**
     * Recherche une transaction en fonction de sa catégorie.
     * @param id de la catégorie à rechercher
     * @return les transactions portant l'id de la catégorie passé en paramètre
     */
    public List<Transaction> findByCategory(Long id) {
        return transactionRepository.findByCategory(id);
    }


    /**
     * Recherche une transaction en fonction de la date de transaction.
     * @param strDateTime date de la transaction à rechercher
     * @return les transactions se situant à la date passé en paramètre
     */
    public List<Transaction> findByDate(String strDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime dateTime = LocalDateTime.parse(strDateTime, formatter);

        return transactionRepository.findByDateTransaction(dateTime);
    }

    /**
     * supprime une transaction en fonction de son id.
     * @param id de la transaction à supprimer.
     */
    public void deleteById(Long id) {
        transactionRepository.deleteById(id);
    }

    /**
     * Ajoute une catégorie à la transaction
     * @param transactionId  transaction à modifier
     * @param categoryId catégorie à ajouter à la transaction
     * @return la transaction avec les nouvelles données.
     */
    public Transaction addCategory(long transactionId, long categoryId) {
        Category category = categoryService.findById(categoryId);
        Transaction transaction = findById(transactionId);

        transaction.setCategory(category);

        return this.transactionRepository.save(transaction);
    }
}
