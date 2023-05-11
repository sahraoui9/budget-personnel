package bgpersonnel.budget.category;

import bgpersonnel.budget.authentification.signin.JwtResponse;
import bgpersonnel.budget.fixtures.UserAuthFixture;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // disable the security filters
@Transactional
@ActiveProfiles("test")
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    UserAuthFixture userAuthFixture;
    private final ObjectMapper mapper = new ObjectMapper();
    private JwtResponse jwtResponse;

    @BeforeEach
    void seTup() {
        jwtResponse = userAuthFixture.createUserAndConnect();
    }

    @Test
    void testCreateCategory() throws Exception {
        // Créer une nouvelle catégorie
        Category category = new Category();
        category.setName("Test Category");
        mockMvc.perform(post("/categories")
                        .header("Authorization", "Bearer " + jwtResponse.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(category)))
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    void testUpdateCategory() throws Exception {
        // creation d'une catégorie
        Category category = new Category();
        category.setName("origin Test Category");
        categoryRepository.save(category);

        // Mettre à jour une catégorie existante
        Category toUpdateCategory = new Category();
        toUpdateCategory.setId(category.getId());
        toUpdateCategory.setName("updated Test Category");
        // Envoyer la requête PUT pour mettre à jour la catégorie
        mockMvc.perform(put("/categories/{id}", toUpdateCategory.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(toUpdateCategory)))
                .andExpect(status().isOk());

        assertEquals("updated Test Category", categoryRepository.findById(toUpdateCategory.getId()).get().getName());
    }

    @Test
    void testDeleteCategory() throws Exception {
        // creation d'une catégorie
        Long categoryId = 1L;

        Category category = new Category();
        category.setId(categoryId);
        category.setName("origin Test Category");
        categoryRepository.save(category);

        // Envoyer la requête DELETE pour supprimer la catégorie
        mockMvc.perform(delete("/categories/{id}", categoryId)
                        .header("Authorization", "Bearer " + jwtResponse.getToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertTrue(categoryRepository.findById(categoryId).isEmpty());
    }

    @Test
    void testGetAllCategories() throws Exception {
        // given
        // Récupérer toutes les catégories
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Category 1");

        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Category 2");

        List<Category> categories = List.of(category1, category2);
        categoryRepository.save(category1);
        categoryRepository.save(category2);

        // when
        mockMvc.perform(get("/categories")
                        .header("Authorization", "Bearer " + jwtResponse.getToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // then
        assertEquals(categories.size(), categoryRepository.findAll().size());
    }

}