package bgpersonnel.budget.authentification.signup;

import bgpersonnel.budget.authentification.common.entity.ERole;
import bgpersonnel.budget.authentification.common.entity.Role;
import bgpersonnel.budget.authentification.common.repository.RoleRepository;
import bgpersonnel.budget.authentification.common.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // disable the security filters
@Transactional
@ActiveProfiles("test")
class SignupControllerTest {


    @Autowired
    private SignupService signupService;

    @Autowired
    MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        Role role = new Role();
        role.setName(ERole.ROLE_USER);
        roleRepository.save(role);
    }

    @Test
    void registerUser_validSignupRequest_shouldReturnOkResponse() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("lahcen@mail.com");
        signupRequest.setName("lahcen");
        signupRequest.setPassword("password");
        signupRequest.setRole(Set.of("ROLE_USER"));
        System.out.println(signupRequest);
        MvcResult result = mvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isOk())
                .andReturn();

        // then
        boolean userExist = userRepository.findByEmail("lahcen@mail.com").isPresent();
        assertThat(userExist).isTrue();

    }

    @DisplayName("test signup with invalid data")
    @Test
    void registerUser_invalidSignupRequest_shouldReturnBadRequest() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test");
        signupRequest.setName("test");
        signupRequest.setRole(Set.of("ROLE_USER"));

        // when
        MvcResult result = mvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isBadRequest())
                .andReturn();
        // then

        String response = result.getResponse().getContentAsString();
        assertThat(response).contains("{\"data\":{\"password\":\"must not be blank\",\"email\":\"must be a well-formed email address\"}}");
    }

}