package bgpersonnel.budget.objectif;

import bgpersonnel.budget.authentification.common.entity.ERole;
import bgpersonnel.budget.authentification.common.entity.Role;
import bgpersonnel.budget.authentification.common.entity.User;
import bgpersonnel.budget.authentification.common.services.UserService;
import bgpersonnel.budget.budget.MailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

class ObjectifTest {

    @Mock
    private ObjectifRepository objectifRepository;

    private ObjectifService objectifService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        UserService userService = Mockito.mock(UserService.class);
        MailService mailService = Mockito.mock(MailService.class);
        user = createUser();
        //when(userService.getConnectedUser()).thenReturn(user);
        objectifService = new ObjectifService(objectifRepository, mailService);
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
    @DisplayName("Création d'un objectif")
    void createObjectif() {
        Objectif objectif = new Objectif();
        objectif.setId(1L);
        objectif.setAmount(1000.0);
        objectif.setName("Test");
        objectif.setDescription("Test unitaire");
        objectif.setTerm(LocalDateTime.now().plusDays(1));
        objectif.setReached(false);

        objectifService.createObjectif(objectif);
        verify(objectifRepository, times(1)).save(objectif);

        assertEquals(1L, objectif.getId());
        assertEquals("Test", objectif.getName());
        assertEquals("Test unitaire", objectif.getDescription());
        assertEquals(1000.0, objectif.getAmount());
        assertFalse(objectif.isReached());
    }


    @Test
    @DisplayName("Modification d'un objectif")
    void updateObjectifTest() {
        Objectif objectif = new Objectif();
        objectif.setId(1L);
        objectif.setAmount(1000.0);
        objectif.setName("Test");
        objectif.setDescription("Test unitaire");
        objectif.setTerm(LocalDateTime.now().plusDays(1));
        objectif.setReached(false);


        objectifService.createObjectif(objectif);
        verify(objectifRepository, times(1)).save(objectif);

        objectif.setName("Test2");
        objectif.setDescription("Test unitaire 2");
        objectif.setAmount(200.0);
        objectifService.update(objectif);

        assertEquals(1L, objectif.getId());
        assertEquals("Test2", objectif.getName());
        assertEquals("Test unitaire 2", objectif.getDescription());
        assertEquals(200.0, objectif.getAmount());
        assertFalse(objectif.isReached());
    }

    @Test
    @DisplayName("Récupération d'un objectif")
    void findByIdTest() {
        Objectif objectif = new Objectif();
        objectif.setId(1L);
        objectif.setAmount(1000.0);
        objectif.setName("Test");
        objectif.setDescription("Test unitaire");
        objectif.setTerm(LocalDateTime.now().plusDays(1));
        objectif.setReached(false);
        when(objectifRepository.findById(1L)).thenReturn(java.util.Optional.of(objectif));
        objectifService.findById(1L);
        verify(objectifRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Récupération de tous les objectifs")
    void findAllTest() {
        objectifService.findAll();
        verify(objectifRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Suppression d'un objectif")
    void deleteObjectifTest() {
        objectifService.deleteById(1L);

        verify(objectifRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Calcul du pourcentage d'un objectif")
    void calculateProgressPercentageTest() {
        Objectif objectif = new Objectif();
        objectif.setId(1L);
        objectif.setAmount(1000.0);
        objectif.setName("Test");
        objectif.setDescription("Test unitaire");
        objectif.setTerm(LocalDateTime.now().plusDays(1));
        objectif.setReached(false);

        when(objectifRepository.findById(1L)).thenReturn(java.util.Optional.of(objectif));
        double result = objectifService.calculateProgressPercentage(1L);
        assertEquals(0.0, result);
    }


    @Test
    @DisplayName("Test si un objectif est atteint")
    void isObjectifAtteintTest() {
        Objectif objectif = new Objectif();
        objectif.setId(1L);
        objectif.setAmount(1000.0);
        objectif.setName("Test");
        objectif.setDescription("Test unitaire");
        objectif.setTerm(LocalDateTime.now().plusDays(1));
        objectif.setReached(false);

        when(objectifRepository.findById(1L)).thenReturn(java.util.Optional.of(objectif));
        boolean result = objectifService.isObjectifAtteint(1L);
        assertFalse(result);
    }
}
