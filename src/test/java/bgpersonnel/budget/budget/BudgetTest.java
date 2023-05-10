package bgpersonnel.budget.budget;

import bgpersonnel.budget.authentification.common.entity.ERole;
import bgpersonnel.budget.authentification.common.entity.Role;
import bgpersonnel.budget.authentification.common.entity.User;
import bgpersonnel.budget.authentification.common.services.UserService;
import bgpersonnel.budget.category.Category;
import bgpersonnel.budget.category.CategoryService;
import bgpersonnel.budget.transaction.Transaction;
import bgpersonnel.budget.transaction.TransactionRepository;
import bgpersonnel.budget.transaction.TypeTransaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class BudgetTest {

    @Mock
    private BudgetRepository budgetRepository;

    @InjectMocks
    private BudgetService budgetService;

    @Mock
    private TransactionRepository transactionRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        UserService userService = Mockito.mock(UserService.class);
        MailService mailService = Mockito.mock(MailService.class);
        user = createUser();
        //when(userService.getConnectedUser()).thenReturn(user);
        budgetService = new BudgetService(mailService, budgetRepository);
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

    @Test
    @DisplayName("Création d'un budget")
    public void createBudgetTest() {
        Budget budget = new Budget();
        budget.setId(1L);
        budget.setName("Test");
        budget.setDescription("Test unitaire");
        budget.setMaxAmount(100.0);
        budget.setGlobal(true);
        budget.setType(BudgetType.MENSUEL);

        //when
        budgetService.create(budget);

        //then
        verify(budgetRepository, times(1)).save(budget);

        assertEquals(1L, budget.getId());
        assertEquals("Test", budget.getName());
        assertEquals("Test unitaire", budget.getDescription());
        assertEquals(100.0, budget.getMaxAmount());
        assertEquals(true, budget.isGlobal());
        assertEquals(BudgetType.MENSUEL, budget.getType());
    }


    @Test
    @DisplayName("Modification d'un budget")
    public void updateBudgetTest() {
        Budget budget = new Budget();
        budget.setId(1L);
        budget.setName("Test");
        budget.setDescription("Test unitaire");
        budget.setMaxAmount(100.0);
        budget.setGlobal(true);
        budget.setType(BudgetType.MENSUEL);

        budgetService.create(budget);

        budget.setName("Test2");
        budget.setDescription("Test unitaire 2");
        budget.setMaxAmount(200.0);
        budget.setGlobal(false);
        budget.setType(BudgetType.ANNUEL);
        budgetService.update(budget);

        assertEquals(1L, budget.getId());
        assertEquals("Test2", budget.getName());
        assertEquals("Test unitaire 2", budget.getDescription());
        assertEquals(200.0, budget.getMaxAmount());
        assertEquals(false, budget.isGlobal());
        assertEquals(BudgetType.ANNUEL, budget.getType());
    }


    @Test
    @DisplayName("Récupération d'un budget")
    public void findByIdTest() {
        Budget budget = new Budget();
        budget.setId(1L);
        budget.setName("Test");
        budget.setDescription("Test unitaire");
        budget.setMaxAmount(100.0);
        budget.setGlobal(true);
        budget.setType(BudgetType.MENSUEL);
        when(budgetRepository.findById(1L)).thenReturn(java.util.Optional.of(budget));
        budgetService.findById(1L);
        verify(budgetRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Récupération de tous les budgets")
    public void findAllTest() {
        budgetService.findAll();
        verify(budgetRepository, times(1)).findAll();
    }


    @Test
    @DisplayName("Suppression d'un budget")
    public void deleteBudgetTest() {
        budgetService.deleteById(1L);

        verify(budgetRepository, times(1)).deleteById(1L);
    }


    @Test
    @DisplayName("Calcul budget annuel d'un utilisateur")
    public void getTotalAnnualBudgetsByYearAndUserTest() {

        try (MockedStatic<UserService> utilities = Mockito.mockStatic(UserService.class)) {
            utilities.when(UserService::getIdConnectedUser).thenReturn(1L);
            budgetService.getTotalAnnualBudgetsByYearAndUser();
        }

        verify(budgetRepository, times(1)).getTotalAnnualBudgetsByYearAndUser(1L);
    }

    @Test
    @DisplayName("Calcul budget d'une année précise d'un utilisateur")
    public void calculateAnnualGlobalBudgetForYearTest() {
        try (MockedStatic<UserService> utilities = Mockito.mockStatic(UserService.class)) {
            utilities.when(UserService::getIdConnectedUser).thenReturn(1L);
            budgetService.calculateAnnualGlobalBudgetForYear(2022);
        }
        verify(budgetRepository, times(1)).calculateAnnualGlobalBudgetForYear(2022, 1L);
    }

    @Test
    @DisplayName("Calcul budget Global d'un utilisateur")
    public void calculateGlobalBudgetTest() {
        try (MockedStatic<UserService> utilities = Mockito.mockStatic(UserService.class)) {
            utilities.when(UserService::getIdConnectedUser).thenReturn(1L);
            budgetService.calculateGlobalBudget();
        }
        verify(budgetRepository, times(1)).calculateGlobalBudget(1L);
    }

    @Test
    @DisplayName("Calcul le budget global pour un mois et une année d'un utilisateur précis")
    public void calculateMonthlyGlobalBudgetForYearAndMonthTest() {
        try (MockedStatic<UserService> utilities = Mockito.mockStatic(UserService.class)) {
            utilities.when(UserService::getIdConnectedUser).thenReturn(1L);
            budgetService.calculateMonthlyGlobalBudgetForYearAndMonth(2, 2023);
        }
        verify(budgetRepository, times(1)).calculateMonthlyGlobalBudgetForYearAndMonth(1L, 2, 2023);
    }

    @Test
    @DisplayName("Calcul le budget global pour chaque mois d'une année et pour un utilisateur précis")
    public void calculateMonthlyGlobalBudgetForYearTest() {
        try (MockedStatic<UserService> utilities = Mockito.mockStatic(UserService.class)) {
            utilities.when(UserService::getIdConnectedUser).thenReturn(1L);
            budgetService.calculateMonthlyGlobalBudgetForYear(2023);
        }
        verify(budgetRepository, times(1)).calculateMonthlyGlobalBudgetForYear(2023, 1L);
    }

    @Test
    @DisplayName("Calcul le budget global pour chaque mois d'une année pour une catégorie et un utilisateur précis")
    public void calculateMonthlyBudgetForYearAndCategoryTest(){
        try (MockedStatic<UserService> utilities = Mockito.mockStatic(UserService.class)) {
            utilities.when(UserService::getIdConnectedUser).thenReturn(1L);
            budgetService.calculateMonthlyBudgetForYearAndCategory(2023, 1L);
        }
        verify(budgetRepository, times(1)).calculateMonthlyBudgetForYearAndCategory(2023, 1L, 1L);

    }

    @Test
    @DisplayName("Calcul le budget global pour chaque mois d'une année et un mois pour une catégorie et un utilisateur précis")
    public void calculateMonthlyBudgetForYearAndMonthAndCategoryTest(){
        try (MockedStatic<UserService> utilities = Mockito.mockStatic(UserService.class)) {
            utilities.when(UserService::getIdConnectedUser).thenReturn(1L);
            budgetService.calculateMonthlyBudgetForYearAndMonthAndCategory(2,2023, 1L);
        }
        verify(budgetRepository, times(1)).calculateMonthlyBudgetForYearAndMonthAndCategory(1L,2,2023,  1L);
    }

    @Test
    @DisplayName("Calcul le budget de chaque années pour une catégorie et un utilisateur précis")
    public void getTotalAnnualBudgetsByYearAndUserAndCategoryTest(){
        try (MockedStatic<UserService> utilities = Mockito.mockStatic(UserService.class)) {
            utilities.when(UserService::getIdConnectedUser).thenReturn(1L);
            budgetService.getTotalAnnualBudgetsByYearAndUserAndCategory(1L);
        }
        verify(budgetRepository, times(1)).getTotalAnnualBudgetsByYearAndUserAndCategory(1L, 1L);
    }

    @Test
    @DisplayName("Calcul le budget de chaque mois d'une année pour une catégorie et un utilisateur précis")
    public void calculateAnnualBudgetForYearAndCategoryTest(){
        try(MockedStatic<UserService> utilities = Mockito.mockStatic(UserService.class)){
            utilities.when(UserService::getIdConnectedUser).thenReturn(1L);
            budgetService.calculateAnnualBudgetForYearAndCategory(2023, 1L);
        }
        verify(budgetRepository, times(1)).calculateAnnualBudgetForYearAndCategory(2023, 1L, 1L);
    }

    @Test
    @DisplayName("Calcul le budget pour une catégorie et un utilisateur précis")
    public void calculateBudgetForCategoryTest(){
        try(MockedStatic<UserService> utilities = Mockito.mockStatic(UserService.class)){
            utilities.when(UserService::getIdConnectedUser).thenReturn(1L);
            budgetService.calculateBudgetForCategory(1L);
        }
        verify(budgetRepository, times(1)).calculateBudgetForCategory(1L, 1L);
    }


    @Test
    @DisplayName("Calcul si le budget est dépassé (test global annuel)")
    public void isBudgetDepasseTestGlobalAnnuel(){
        Category category = new Category();
        category.setId(1L);
        category.setName("Categorie1");
        Budget budget = new Budget();
        budget.setId(1L);
        budget.setName("Test");
        budget.setDescription("Test unitaire");
        budget.setMaxAmount(100.0);
        budget.setGlobal(true);
        budget.setType(BudgetType.ANNUEL);
        budget.setCategory(category);

        when(budgetRepository.findById(1L)).thenReturn(java.util.Optional.of(budget));
        boolean bugdetDepasseTest= true;
        try(MockedStatic<UserService> utilities = Mockito.mockStatic(UserService.class)){
            utilities.when(UserService::getIdConnectedUser).thenReturn(1L);
            bugdetDepasseTest = budgetService.isBudgetDepasse(1L);
        }

        assertEquals(false,bugdetDepasseTest);

    }

    @Test
    @DisplayName("Calcul si le budget est dépassé (test global mensuel)")
    public void isBudgetDepasseTestGlobalMensuel(){
        Category category = new Category();
        category.setId(1L);
        category.setName("Categorie1");
        Budget budget = new Budget();
        budget.setId(1L);
        budget.setName("Test");
        budget.setDescription("Test unitaire");
        budget.setMaxAmount(100.0);
        budget.setGlobal(true);
        budget.setType(BudgetType.MENSUEL);
        budget.setCategory(category);

        when(budgetRepository.findById(1L)).thenReturn(java.util.Optional.of(budget));
        boolean bugdetDepasseTest= true;
        try(MockedStatic<UserService> utilities = Mockito.mockStatic(UserService.class)){
            utilities.when(UserService::getIdConnectedUser).thenReturn(1L);
            bugdetDepasseTest = budgetService.isBudgetDepasse(1L);
        }

        assertEquals(false,bugdetDepasseTest);

    }


    @Test
    @DisplayName("Calcul si le budget est dépassé (test global mensuel depasse)")
    public void isBudgetDepasseTestGlobalMensuelDepasse(){
        Category category = new Category();
        category.setId(1L);
        category.setName("Categorie1");
        Budget budget = new Budget();
        budget.setId(1L);
        budget.setName("Test");
        budget.setDescription("Test unitaire");
        budget.setMaxAmount(100.0);
        budget.setGlobal(true);
        budget.setUser(user);
        budget.setType(BudgetType.ANNUEL);
        budget.setCategory(category);

        when(budgetRepository.findById(1L)).thenReturn(java.util.Optional.of(budget));

        boolean bugdetDepasseTest= true;
        try(MockedStatic<UserService> utilities = Mockito.mockStatic(UserService.class)){
            utilities.when(UserService::getIdConnectedUser).thenReturn(1L);
            when(budgetRepository.calculateAnnualGlobalBudgetForYear(2023,  1L)).thenReturn(115.0);
            bugdetDepasseTest = budgetService.isBudgetDepasse(1L);
        }

        assertEquals(true,bugdetDepasseTest);

    }


    @Test
    @DisplayName("Calcul des ajustements")
    public void calculateAdjustmentsTest(){
        List<Map<String, Object>> dataList = new ArrayList<>();
        List<Map<String, Object>> Result = new ArrayList<>();

        // Création du premier élément
        Map<String, Object> data1 = new HashMap<>();
        data1.put("categ", 1L);
        data1.put("month", 1);
        data1.put("totalBudget", 100);
        data1.put("budgetAmount", 150);
        dataList.add(data1);

        // Création du deuxième élément
        Map<String, Object> data2 = new HashMap<>();
        data2.put("categ", 1L);
        data2.put("month", 2);
        data2.put("totalBudget", 50);
        data2.put("budgetAmount", 150);
        dataList.add(data2);

        // Création du troisième élément
        Map<String, Object> data3 = new HashMap<>();
        data3.put("categ", 1L);
        data3.put("month", 3);
        data3.put("totalBudget", 200);
        data3.put("budgetAmount", 150);
        dataList.add(data3);



        try(MockedStatic<UserService> utilities = Mockito.mockStatic(UserService.class)) {
            utilities.when(UserService::getIdConnectedUser).thenReturn(1L);
            when(budgetRepository.calculateMonthlyGlobalBudgetForYearAndMonth(1L, 4, 2023)).thenReturn(200.0);
            when(budgetRepository.calculateMonthlyBudgetByMounthForYearForCategory(2023, 1L)).thenReturn(dataList);
            Result = budgetService.calculateAdjustments();
        }

        assertEquals(Result.get(0).get("fromCategory"), 1L);
        assertEquals(Result.get(0).get("amountAdjustemnt"), -25.0);
    }
}
