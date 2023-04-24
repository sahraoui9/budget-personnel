package bgpersonnel.budget.services;

import bgpersonnel.budget.entity.User;
import bgpersonnel.budget.exeception.AlreadyExistsException;
import bgpersonnel.budget.payload.request.LoginRequest;
import bgpersonnel.budget.payload.request.SignupRequest;
import bgpersonnel.budget.payload.response.JwtResponse;
import bgpersonnel.budget.repository.RoleRepository;
import bgpersonnel.budget.repository.UserRepository;
import bgpersonnel.budget.security.JwtUtils;
import bgpersonnel.budget.security.services.RefreshTokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

// tester class for AuthService
@SpringBootTest
@Transactional
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Test
    public void testSignInWithCorrectCredentials() {

        SignupRequest signupRequest = createSignupRequest();
        authService.registerUser(signupRequest);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("test_user");
        loginRequest.setPassword("test_password");

        JwtResponse jwtResponse = authService.signIn(loginRequest);

        assertNotNull(jwtResponse);
        assertNotNull(jwtResponse.getRefreshToken());
        assertEquals("test_user", jwtResponse.getUsername());
    }

    @Test
    public void testSignInWithIncorrectCredentials() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("wrongpassword");
        assertThrows(BadCredentialsException.class, () -> authService.signIn(loginRequest));
    }

    @Test
    void registerUser() {
        String username = "test_user";
        String email = "test_user@example.com";
        String password = "test_password";
        Set<String> roles = new HashSet<>();
        roles.add("user");

        SignupRequest signupRequest = new SignupRequest(username, email, roles, password);
        authService.registerUser(signupRequest);
    }

    @Test
    public void testRegisterUserAlreadyExists() {
        // create a new user
        SignupRequest signupRequest = createSignupRequest();
        authService.registerUser(signupRequest);
        Exception exception = assertThrows(AlreadyExistsException.class, () -> authService.registerUser(signupRequest));
        assertTrue(exception.getMessage().contains("Error: Username is already taken!"));
    }

    @Test
    public void testRegisterUser() {
        SignupRequest signupRequest = createSignupRequest();
        authService.registerUser(signupRequest);

        // verify that the user was created successfully
        assertTrue(userRepository.existsByUsername(signupRequest.getUsername()));
        User user = userRepository.findByUsername(signupRequest.getUsername()).orElse(null);
        assertNotNull(user);
        assertEquals(user.getEmail(), signupRequest.getEmail());
        assertTrue(passwordEncoder.matches(signupRequest.getPassword(), user.getPassword()));
        System.out.println(user.getId());

    }

    private SignupRequest createSignupRequest() {
        String username = "test_user";
        String email = "test_user@example.com";
        String password = "test_password";
        Set<String> roles = new HashSet<>();
        roles.add("user");

        return new SignupRequest(username, email, roles, password);
    }
}