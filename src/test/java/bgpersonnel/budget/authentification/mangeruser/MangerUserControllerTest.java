package bgpersonnel.budget.authentification.mangeruser;

import bgpersonnel.budget.authentification.signin.JwtResponse;
import bgpersonnel.budget.fixtures.UserAuthFixture;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class MangerUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    UserAuthFixture userAuthFixture;
    private JwtResponse jwtResponse;

    @BeforeEach
    public void setUp() throws Exception {

        jwtResponse = userAuthFixture.createUserAndConnect();
    }

    @Test
    @DisplayName("Test update user successfully")
    void updateUser() throws Exception {
        //given
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
        userUpdateRequest.setEmail("userUpdated@mail.com");
        userUpdateRequest.setName("userUpdated");

        //when
        MvcResult result = mockMvc.perform(put("/api/users/update-user")
                        // set berear token
                        .header("Authorization", "Bearer " + jwtResponse.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdateRequest)))
                .andExpect(status().isOk())
                .andReturn();
        //then
        Assertions.assertEquals("User information updated successfully!", result.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("Test update password successfully")
    void updatePassword() throws Exception {
        //given
        UpdatedPassword password = new UpdatedPassword();
        password.setOldPassword("password");
        password.setNewPassword("newPassword");

        //when
        MvcResult result = mockMvc.perform(put("/api/users/update-password")
                        .header("Authorization", "Bearer " + jwtResponse.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(password)))
                .andExpect(status().isOk())
                .andReturn();
        //then
        Assertions.assertEquals("Password updated successfully!", result.getResponse().getContentAsString());
    }
}