package bgpersonnel.budget.category;

import bgpersonnel.budget.authentification.signin.JwtResponse;
import bgpersonnel.budget.fixtures.UserAuthFixture;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    UserAuthFixture userAuthFixture;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CategoryRepository categoryRepository;

    private Category category;
    private JwtResponse jwtResponse;

    @BeforeEach
    void seTup() {
        jwtResponse = userAuthFixture.createUserAndConnect();
        category = new Category();
        category.setName("origin Test Category");
        category.setUser(userAuthFixture.getUser());
        category.setCreatedAt(LocalDateTime.now());
        categoryRepository.save(category);
    }

    @Test
    @DisplayName("Test create category")
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
    @DisplayName("Test update category")
    void testUpdateCategory() throws Exception {

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
    @DisplayName("Test delete category")
    void testDeleteCategory() throws Exception {
        // given
        Long categoryId = 5L;

        Category category = new Category();
        category.setId(categoryId);
        category.setName("origin Test Category");
        categoryRepository.save(category);
        //when

        // Envoyer la requête DELETE pour supprimer la catégorie
        mockMvc.perform(delete("/categories/{id}", categoryId)
                        .header("Authorization", "Bearer " + jwtResponse.getToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        // then
        assertTrue(categoryRepository.findById(categoryId).isEmpty());
    }

    @Test
    @DisplayName("Test get all categories")
    void testGetAllCategories() throws Exception {
        // given
        // Récupérer toutes les catégories
        Category category1 = new Category();
        category1.setName("Category 1");

        Category category2 = new Category();
        category2.setName("Category 2");

        List<Category> categories = List.of(category1, category2, category);
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

    @Test
    @DisplayName("Test get category by id")
    void testGetCategoryById() throws Exception {
        // given

        // when
        mockMvc.perform(get("/categories/{id}", category.getId())
                        .header("Authorization", "Bearer " + jwtResponse.getToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // then
        assertEquals(category.getName(), categoryRepository.findById(category.getId()).get().getName());
    }


}