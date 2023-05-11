package bgpersonnel.budget.authentification.signin;


import bgpersonnel.budget.authentification.common.entity.User;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // disable the security filters
@Transactional
@ActiveProfiles("test")
class SigninControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setEmail("user@mail.com");
        user.setName("user");
        user.setPassword(passwordEncoder.encode("password"));
        userRepository.save(user);
    }

    @DisplayName("Test authenticate user successfully")
    @Test
    void testAuthenticateUser() throws Exception {
        // given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("user@mail.com");
        loginRequest.setPassword("password");

        // when
        MvcResult result = mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();
        // then
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(result.getResponse().getContentAsString()).isNotEmpty();
        assertThat(result.getResponse().getContentAsString()).contains("token");
        assertThat(result.getResponse().getContentAsString()).contains("refreshToken");
        assertThat(result.getResponse().getContentAsString()).contains(loginRequest.getEmail());
    }

    @DisplayName("Test authenticate user with wrong password")
    @Test
    void testAuthenticateUserWithWrongPassword() throws Exception {
        // given
        LoginRequest loginRequest = new LoginRequest("user@mail.com", "wrongPassword");

        // when
        MvcResult result = mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andReturn();
        // then
        assertThat(result.getResponse().getStatus()).isEqualTo(400);
        assertThat(result.getResponse().getContentAsString()).isNotEmpty();
        assertThat(result.getResponse().getContentAsString()).contains("Bad credentials");
    }
}
