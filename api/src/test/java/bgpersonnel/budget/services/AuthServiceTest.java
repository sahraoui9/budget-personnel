package bgpersonnel.budget.services;

import bgpersonnel.budget.authentification.entity.User;
import bgpersonnel.budget.authentification.services.AuthService;
import bgpersonnel.budget.exeception.AlreadyExistsException;
import bgpersonnel.budget.authentification.payload.request.LoginRequest;
import bgpersonnel.budget.authentification.payload.request.SignupRequest;
import bgpersonnel.budget.authentification.payload.response.JwtResponse;
import bgpersonnel.budget.authentification.repository.RoleRepository;
import bgpersonnel.budget.authentification.repository.UserRepository;
import bgpersonnel.budget.authentification.security.JwtUtils;
import bgpersonnel.budget.authentification.security.services.RefreshTokenService;
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
        loginRequest.setEmail("test_user@example.com");
        loginRequest.setPassword("test_password");

        JwtResponse jwtResponse = authService.signIn(loginRequest);

        assertNotNull(jwtResponse);
        assertNotNull(jwtResponse.getRefreshToken());
        assertEquals("test_user@example.com", jwtResponse.getEmail());
    }

    @Test
    public void testSignInWithIncorrectCredentials() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test_user@example.com");
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
        User user = userRepository.findByEmail(signupRequest.getEmail()).orElse(null);
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