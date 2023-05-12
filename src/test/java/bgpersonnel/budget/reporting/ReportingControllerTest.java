package bgpersonnel.budget.reporting;

import bgpersonnel.budget.authentification.common.entity.User;
import bgpersonnel.budget.authentification.signin.JwtResponse;
import bgpersonnel.budget.budget.Budget;
import bgpersonnel.budget.budget.BudgetType;
import bgpersonnel.budget.category.Category;
import bgpersonnel.budget.fixtures.UserAuthFixture;
import bgpersonnel.budget.objectif.Objectif;
import bgpersonnel.budget.reporting.depense.ExpenseReport;
import bgpersonnel.budget.transaction.Transaction;
import bgpersonnel.budget.transaction.TypeTransaction;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // disable the security filters
@Transactional
@ActiveProfiles("test")
class ReportingControllerTest {
    @Autowired
    private ReportingService reportingService;
    private JwtResponse jwtResponse;

    @Autowired
    private ExpenseReport expenseReport;
    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private UserAuthFixture userAuthFixture;

    private User user;

    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        jwtResponse = userAuthFixture.createUserAndConnect();
        user = userAuthFixture.getUser();
        createTransactions();
    }

    private void createTransactions() {

        //budget
        Budget budget1 = new Budget();
        budget1.setName("budget1");
        budget1.setDescription("description budget1");
        budget1.setMaxAmount(1000.0);
        budget1.setGlobal(true);
        budget1.setType(BudgetType.ANNUEL);
        budget1.setUser(user);
        budget1.setCreatedAt(LocalDateTime.now());
        entityManager.persist(budget1);

        Budget budget2 = new Budget();
        budget2.setName("budget2");
        budget2.setDescription("description budget2");
        budget2.setMaxAmount(1000.0);
        budget2.setGlobal(true);
        budget2.setType(BudgetType.ANNUEL);
        budget2.setUser(user);
        budget2.setCreatedAt(LocalDateTime.now());
        entityManager.persist(budget2);

        //Category
        Category category1 = new Category();
        category1.setName("category1");
        category1.setUser(user);
        category1.setBudget(budget1);
        category1.setCreatedAt(LocalDateTime.now());
        entityManager.persist(category1);

        Category category2 = new Category();
        category2.setName("category2");
        category2.setUser(user);
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
        depenseObjectif.setUser(user);
        depenseObjectif.setTerm(LocalDateTime.of(2024, 1, 1, 0, 0));
        entityManager.persist(depenseObjectif);

        Objectif revenuObjectif = new Objectif();
        revenuObjectif.setName("revenuObjectif");
        revenuObjectif.setDescription("description revenuObjectif");
        revenuObjectif.setAmount(3000.0);
        revenuObjectif.setCreatedAt(LocalDateTime.now());
        revenuObjectif.setReached(false);
        revenuObjectif.setUser(user);
        revenuObjectif.setTerm(LocalDateTime.of(2024, 1, 1, 0, 0));
        entityManager.persist(revenuObjectif);

        //Transactions
        Transaction transaction = new Transaction();
        transaction.setAmount(275.0);
        transaction.setDescription("Transaction test unitaire");
        transaction.setTypeTransaction(TypeTransaction.DEPENSE);
        transaction.setUser(user);
        transaction.setCategory(category1);
        transaction.setDateTransaction(LocalDate.of(2023, 4, 1));
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setObjectif(depenseObjectif);
        entityManager.persist(transaction);

        Transaction transaction1 = new Transaction();
        transaction1.setAmount(200.0);
        transaction1.setDescription("Transaction test 1");
        transaction1.setTypeTransaction(TypeTransaction.DEPENSE);
        transaction1.setUser(user);
        transaction1.setCategory(category1);
        transaction1.setDateTransaction(LocalDate.of(2023, 3, 1));
        transaction1.setCreatedAt(LocalDateTime.now());
        transaction.setObjectif(depenseObjectif);
        entityManager.persist(transaction1);

        Transaction transaction2 = new Transaction();
        transaction2.setAmount(1200.0);
        transaction2.setDescription("Transaction test 2");
        transaction2.setTypeTransaction(TypeTransaction.REVENU);
        transaction2.setUser(user);
        transaction2.setCategory(category2);
        transaction2.setDateTransaction(LocalDate.of(2023, 3, 1));
        transaction2.setCreatedAt(LocalDateTime.now());
        transaction2.setObjectif(revenuObjectif);
        entityManager.persist(transaction2);

        Transaction transaction3 = new Transaction();
        transaction3.setAmount(300.0);
        transaction3.setDescription("Transaction test 3");
        transaction3.setTypeTransaction(TypeTransaction.REVENU);
        transaction3.setUser(user);
        transaction3.setCategory(category2);
        transaction3.setDateTransaction(LocalDate.of(2023, 3, 1));
        transaction3.setCreatedAt(LocalDateTime.now());
        transaction3.setObjectif(revenuObjectif);
        entityManager.persist(transaction3);

        entityManager.flush();
    }

    @Test
    @DisplayName("Test generateReport() with valid input")
    void testGenerateReport() throws Exception {

        MvcResult response = mockMvc.perform(post("/api/reports")
                        .header("Authorization", "Bearer " + jwtResponse.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"startDate\":\"2001-12-11\",\"endDate\":\"2024-01-31\",\"reportType\":\"CSV\"}"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("attachment; filename=report.csv", response.getResponse().getHeader("Content-Disposition"));
        assertEquals(MediaType.parseMediaType("text/csv"), MediaType.parseMediaType(Objects.requireNonNull(response.getResponse().getContentType())));
    }

    @Test
    @DisplayName("Test generateExpenseReport() with valid input")
    void generateExpenseReport() throws Exception {

        MvcResult response = mockMvc.perform(post("/api/reports/expense-report")
                        .header("Authorization", "Bearer " + jwtResponse.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"PDF\""))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("attachment; filename=report.pdf", response.getResponse().getHeader("Content-Disposition"));
        assertEquals(MediaType.parseMediaType("application/pdf"), MediaType.parseMediaType(Objects.requireNonNull(response.getResponse().getContentType())));
    }

}