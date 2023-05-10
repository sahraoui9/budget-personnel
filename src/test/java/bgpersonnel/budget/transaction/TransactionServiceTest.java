package bgpersonnel.budget.transaction;

import bgpersonnel.budget.authentification.common.entity.ERole;
import bgpersonnel.budget.authentification.common.entity.Role;
import bgpersonnel.budget.authentification.common.entity.User;
import bgpersonnel.budget.authentification.common.services.UserService;
import bgpersonnel.budget.budget.BudgetService;
import bgpersonnel.budget.category.Category;
import bgpersonnel.budget.category.CategoryService;
import bgpersonnel.budget.objectif.ObjectifService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    Transaction transaction;

    private TransactionService transactionService;

    private UserService userService;

    private CategoryService categoryService;

    private TransactionRepository transactionRepository;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        ObjectifService objectifService = Mockito.mock(ObjectifService.class);
        BudgetService budgetService = Mockito.mock(BudgetService.class);
        transactionRepository = Mockito.mock(TransactionRepository.class);
        userService = Mockito.mock(UserService.class);
        categoryService = Mockito.mock(CategoryService.class);
        transactionService = new TransactionService(
                transactionRepository,
                userService,
                categoryService,
                objectifService,
                budgetService
        );

        this.transaction = new Transaction();
        this.transaction.setId(1L);
        this.transaction.setAmount(275.0);
        this.transaction.setDateTransaction(LocalDate.now());
        this.transaction.setTypeTransaction(TypeTransaction.DEPENSE);
        this.transaction.setDescription("Transaction test unitaire");

        Mockito.when(transactionRepository.findById(1L)).thenReturn(Optional.of(this.transaction));
        SecurityContextHolder.setContext(new SecurityContextImpl());
    }

    private User createUser() {
        User user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setRoles(Set.of(new Role(1, ERole.ROLE_USER)));

        return user;
    }

    @DisplayName("Création d'un transaction")
    @Test
    void createTransactionTest() {

        when(userService.getConnectedUser()).thenReturn(createUser());
        when(transactionRepository.save(transaction)).thenReturn(transaction);
        transactionService.create(transaction);

        verify(transactionRepository, times(1)).save(transaction);
        verify(userService, times(1)).getConnectedUser();
        assertEquals(1L, transaction.getUser().getId());
        assertNotNull(transaction.getCreatedAt());
        assertNotNull(transaction.getCreatedBy());
    }

    @Test
    @DisplayName("Modification d'une transaction")
    void updateTransactionTest() {
        User user = createUser();
        transaction.setUser(user);

        when(userService.getConnectedUser()).thenReturn(user);
        transactionService.update(transaction);

        assertNotNull(transaction.getUpdatedAt());
        assertNotNull(transaction.getUpdatedBy());
    }

    @Test
    @DisplayName("Doit retourner une transaction avec une catégorie")
    void addCategoryTest() {
        Category category = new Category();
        category.setId(1L);
        category.setName("CategorieTest");
        when(categoryService.findById(1L)).thenReturn(category);

        transactionService.addCategory(1L, 1L);
        assertEquals("CategorieTest", transaction.getCategory().getName());
        assertEquals(1L, transaction.getCategory().getId());
        assertEquals(TypeTransaction.DEPENSE, transaction.getTypeTransaction());
        assertEquals("Dépense", transaction.getTypeTransaction().getName());
    }

    @DisplayName("Recherche de transactions par utilisateur")
    @Test
    void findByCategory_ShouldReturnListOfTransactions() {
        // given
        User user = createUser();
        Long categoryId = 1L;

        Category category = new Category();
        category.setId(categoryId);

        // when
        when(userService.getConnectedUser()).thenReturn(user);
        when(categoryService.findById(categoryId)).thenReturn(category);
        when(transactionRepository.findByCategoryAndUser(category, user)).thenReturn(List.of(
                createTransaction(category, 1L, user),
                createTransaction(category, 2L, user)
        ));

        List<Transaction> result = transactionService.findByCategory(1L);

        // then
        Assertions.assertThat(result).hasSize(2);
        Assertions.assertThat(result.get(0).getId()).isEqualTo(1L);
        Assertions.assertThat(result.get(1).getId()).isEqualTo(2L);
        verify(userService).getConnectedUser();
        verify(categoryService).findById(categoryId);
        verify(transactionRepository).findByCategoryAndUser(category, user);
    }

    private Transaction createTransaction(Category category, Long transactionId, User user) {

        Transaction transaction3 = new Transaction();
        transaction3.setId(transactionId);
        transaction3.setAmount(275.0);
        transaction3.setDescription("Transaction test 3");
        transaction3.setTypeTransaction(TypeTransaction.REVENU);
        transaction3.setUser(user);
        transaction3.setCategory(category);
        transaction3.setDateTransaction(LocalDate.of(2023, 02, 01));
        transaction3.setCreatedAt(LocalDateTime.now());
        return transaction3;
    }


    @DisplayName("Recherche de transactions par date et par utilisateur")
    @Test
    void testFindByDate() {
        User user = createUser();
        // Création de transactions avec des dates spécifiques
        Transaction t1 = new Transaction(1L, 100.0, "description 1"
                , LocalDate.of(2022, Month.JANUARY, 1), TypeTransaction.REVENU, user, new Category(), null);
        Transaction t2 = new Transaction(2L, 100.0, "description 2"
                , LocalDate.of(2022, Month.FEBRUARY, 15), TypeTransaction.DEPENSE, user, new Category(), null);
        Transaction t3 = new Transaction(3L, 100.0, "description 1"
                , LocalDate.of(2022, Month.MARCH, 31), TypeTransaction.REVENU, user, new Category(), null);
        List<Transaction> transactions = Arrays.asList(t1, t2, t3);

        // Simulation du retour de la méthode findByDateTransactionAndUser du repository
        when(transactionRepository.findByDateTransactionAndUser(any(LocalDate.class), any(User.class))).thenReturn(
                Arrays.asList(t3));
        when(userService.getConnectedUser()).thenReturn(user);
        // Appel de la méthode findByDate à tester
        List<Transaction> result = transactionService.findByDate("2022-03-31");

        // Vérification que les transactions retournées ont bien la date demandée
        assertEquals(1, result.size());
        assertEquals(t3, result.get(0));
        // Vérification que la méthode findByDateTransactionAndUser du repository a bien été appelée
        verify(transactionRepository, times(1)).findByDateTransactionAndUser(LocalDate.of(2022, Month.MARCH, 31), user);
    }

}
