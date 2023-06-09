package bgpersonnel.budget;


import bgpersonnel.budget.authentification.common.entity.ERole;
import bgpersonnel.budget.authentification.common.entity.Role;
import bgpersonnel.budget.authentification.common.entity.User;
import bgpersonnel.budget.budget.Budget;
import bgpersonnel.budget.budget.BudgetType;
import bgpersonnel.budget.category.Category;
import bgpersonnel.budget.objectif.Objectif;
import bgpersonnel.budget.transaction.Transaction;
import bgpersonnel.budget.transaction.TypeTransaction;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Profile("dev")
@Component
public class InitDb implements CommandLineRunner {
    PasswordEncoder encoder;
    @PersistenceContext
    private EntityManager entityManager;

    public InitDb(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        Role roleUser = new Role();
        roleUser.setName(ERole.ROLE_USER);
        entityManager.persist(roleUser);

        Role roleAdmin = new Role();
        roleAdmin.setName(ERole.ROLE_ADMIN);
        entityManager.persist(roleAdmin);

        // Users
        String password = encoder.encode("password");

        User user1 = new User();
        user1.setName("user1");
        user1.setEmail("user@mail.com");
        user1.setPassword(password);
        user1.getRoles().add(roleUser);
        entityManager.persist(user1);
        //budget
        Budget budget1 = new Budget();
        budget1.setName("budget1");
        budget1.setDescription("description budget1");
        budget1.setMaxAmount(1000.0);
        budget1.setGlobal(true);
        budget1.setType(BudgetType.ANNUEL);
        budget1.setUser(user1);
        budget1.setCreatedAt(LocalDateTime.now());
        entityManager.persist(budget1);

        Budget budget2 = new Budget();
        budget2.setName("budget2");
        budget2.setDescription("description budget2");
        budget2.setMaxAmount(1000.0);
        budget2.setGlobal(true);
        budget2.setType(BudgetType.ANNUEL);
        budget2.setUser(user1);
        budget2.setCreatedAt(LocalDateTime.now());
        entityManager.persist(budget2);

        //Category
        Category category1 = new Category();
        category1.setName("category1");
        category1.setUser(user1);
        category1.setBudget(budget1);
        category1.setCreatedAt(LocalDateTime.now());
        entityManager.persist(category1);

        Category category2 = new Category();
        category2.setName("category2");
        category2.setUser(user1);
        category2.setBudget(budget2);
        category2.setCreatedAt(LocalDateTime.now());
        entityManager.persist(category2);

        //Objectif
        Objectif depenseObjectif = new Objectif();
        depenseObjectif.setName("depenseObjectif");
        depenseObjectif.setDescription("description depenseObjectif");
        depenseObjectif.setAmount(1000.0);
        depenseObjectif.setCreatedAt(LocalDateTime.now());
        depenseObjectif.setReached(false);
        depenseObjectif.setUser(user1);
        depenseObjectif.setTerm(LocalDateTime.of(2024, 1, 1, 0, 0));
        entityManager.persist(depenseObjectif);

        Objectif revenuObjectif = new Objectif();
        revenuObjectif.setName("revenuObjectif");
        revenuObjectif.setDescription("description revenuObjectif");
        revenuObjectif.setAmount(3000.0);
        revenuObjectif.setCreatedAt(LocalDateTime.now());
        revenuObjectif.setReached(false);
        revenuObjectif.setUser(user1);
        revenuObjectif.setTerm(LocalDateTime.of(2024, 1, 1, 0, 0));
        entityManager.persist(revenuObjectif);

        //Transactions
        Transaction transaction = new Transaction();
        transaction.setAmount(275.0);
        transaction.setDescription("Transaction test unitaire");
        transaction.setTypeTransaction(TypeTransaction.DEPENSE);
        transaction.setUser(user1);
        transaction.setCategory(category1);
        transaction.setDateTransaction(LocalDate.of(2023, 4, 1));
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setObjectif(depenseObjectif);
        entityManager.persist(transaction);

        Transaction transaction1 = new Transaction();
        transaction1.setAmount(200.0);
        transaction1.setDescription("Transaction test 1");
        transaction1.setTypeTransaction(TypeTransaction.DEPENSE);
        transaction1.setUser(user1);
        transaction1.setCategory(category1);
        transaction1.setDateTransaction(LocalDate.of(2023, 3, 1));
        transaction1.setCreatedAt(LocalDateTime.now());
        transaction.setObjectif(depenseObjectif);
        entityManager.persist(transaction1);

        Transaction transaction2 = new Transaction();
        transaction2.setAmount(1200.0);
        transaction2.setDescription("Transaction test 2");
        transaction2.setTypeTransaction(TypeTransaction.REVENU);
        transaction2.setUser(user1);
        transaction2.setCategory(category1);
        transaction2.setDateTransaction(LocalDate.of(2023, 3, 1));
        transaction2.setCreatedAt(LocalDateTime.now());
        transaction2.setObjectif(revenuObjectif);
        entityManager.persist(transaction2);

        Transaction transaction3 = new Transaction();
        transaction3.setAmount(300.0);
        transaction3.setDescription("Transaction test 3");
        transaction3.setTypeTransaction(TypeTransaction.REVENU);
        transaction3.setUser(user1);
        transaction3.setCategory(category2);
        transaction3.setDateTransaction(LocalDate.of(2023, 3, 1));
        transaction3.setCreatedAt(LocalDateTime.now());
        transaction3.setObjectif(revenuObjectif);
        entityManager.persist(transaction3);


        entityManager.flush();
    }


}
