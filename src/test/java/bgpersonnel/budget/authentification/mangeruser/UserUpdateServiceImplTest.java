package bgpersonnel.budget.authentification.mangeruser;

import bgpersonnel.budget.authentification.common.entity.User;
import bgpersonnel.budget.authentification.common.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserUpdateServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private UserUpdateServiceImpl userUpdateService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testUpdateUserInfo() {
        // given
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
        userUpdateRequest.setName("John");
        userUpdateRequest.setEmail("john@example.com");

        // Mock connected user
        User user = new User();
        user.setId(1L);
        user.setName("Jane");
        user.setEmail("jane@example.com");
        // when
        when(userService.getConnectedUser()).thenReturn(user);
        userUpdateService.updateUserInfo(userUpdateRequest);

        // then
        verify(userService, times(1)).saveUser(user);
        assertEquals(userUpdateRequest.getName(), user.getName());
        assertEquals(userUpdateRequest.getEmail(), user.getEmail());
    }

    @Test
    void testUpdatePassword() {
        // Mock updatedPassword
        UpdatedPassword updatedPassword = new UpdatedPassword();
        updatedPassword.setOldPassword("oldPassword");
        updatedPassword.setNewPassword("newPassword");

        // Mock connected user
        User user = new User();
        user.setId(1L);
        user.setPassword("oldEncodedPassword");

        when(userService.getConnectedUser()).thenReturn(user);
        when(encoder.matches(updatedPassword.getOldPassword(), "oldEncodedPassword")).thenReturn(true);
        when(encoder.encode(updatedPassword.getNewPassword())).thenReturn("newEncodedPassword");

        // Call the method to be tested
        userUpdateService.updatePassword(updatedPassword);

        // Verify that password is updated
        verify(userService, times(1)).saveUser(user);
        assertEquals("newEncodedPassword", user.getPassword());
    }

    @Test
    void testUpdatePasswordWithNotCorrectPassword() {
        // Mock updatedPassword
        UpdatedPassword updatedPassword = new UpdatedPassword();
        updatedPassword.setOldPassword("oldPasswordNotCorrect");
        updatedPassword.setNewPassword("newPassword");

        // Mock connected user
        User user = new User();
        user.setId(1L);
        user.setPassword("oldEncodedPassword");

        when(userService.getConnectedUser()).thenReturn(user);
        when(encoder.matches("oldPasswordNotCorrect", "oldEncodedPassword")).thenReturn(false);

        // Call the method to be tested and verify that it throws an exception
        assertThrows(RuntimeException.class, () -> userUpdateService.updatePassword(updatedPassword));

        // Verify that password is not updated
        verify(userService, times(0)).saveUser(user);
    }
}