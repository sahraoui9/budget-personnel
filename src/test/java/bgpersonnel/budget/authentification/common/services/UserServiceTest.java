package bgpersonnel.budget.authentification.common.services;


import bgpersonnel.budget.authentification.common.entity.User;
import bgpersonnel.budget.authentification.common.repository.UserRepository;
import bgpersonnel.budget.authentification.security.services.UserDetailsImpl;
import bgpersonnel.budget.exeception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    private UserService userService;
    private final String userMail = "test@example.com";
    private final Long userId = 123L;
    private final User user = new User(userId, "Test User", "test@example.com", "password");

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository);

        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(new UserDetailsImpl(user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPassword(), List.of()));

        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @DisplayName("Find user by email")
    @Test
    void findUserByEmailShouldReturnUser() {
        //when
        when(userRepository.findByEmail(userMail)).thenReturn(Optional.of(user));
        User result = userService.findUserByEmail(userMail);
        //then
        assertNotNull(result);
        assertEquals(userId, result.getId());
    }

    @DisplayName("Find user by email should throw NotFoundException")
    @Test
    void findUserByEmailShouldThrowNotFoundException() {
        //when
        when(userRepository.findByEmail(userMail)).thenReturn(Optional.empty());

        //then
        assertThrows(NotFoundException.class, () -> {
            userService.findUserByEmail(userMail);
        });
    }

    @DisplayName("Find user by id should return user")
    @Test
    void findByIdShouldReturnUser() {
        //when
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        User result = userService.findById(userId);
        //then
        assertNotNull(result);
        assertEquals(userMail, result.getEmail());
    }

    @DisplayName("Find user by id should throw ResponseStatusException")
    @Test
    void findByIdShouldThrowResponseStatusException() {
        //when
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        //then
        assertThrows(ResponseStatusException.class, () -> {
            userService.findById(userId);
        });
    }

    @DisplayName("Get connected user should return user")
    @Test
    void getConnectedUserShouldReturnUser() {
        //when
        when(userRepository.findByEmail(userMail)).thenReturn(Optional.of(user));
        User result = userService.getConnectedUser();
        //then
        assertNotNull(result);
        assertEquals(userId, result.getId());
    }

    @DisplayName("Should  Save user")
    @Test
    void saveUserShouldSaveUser() {
        //when
        when(userRepository.save(user)).thenReturn(user);
        userService.saveUser(user);

        //then
        verify(userRepository, times(1)).save(user);
    }
}
