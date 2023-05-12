package bgpersonnel.budget.fixtures;

import bgpersonnel.budget.authentification.common.entity.Role;
import bgpersonnel.budget.authentification.common.entity.User;
import bgpersonnel.budget.authentification.signin.JwtResponse;
import bgpersonnel.budget.authentification.signin.LoginRequest;
import bgpersonnel.budget.authentification.signin.SigninService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


@Component
@Profile("test")
public class UserAuthFixture {


    @PersistenceContext
    private EntityManager em;
    @Autowired
    private SigninService signinService;

    private User user;

    // create user and connected
    public JwtResponse createUserAndConnect() {
        Role role = UserFactory.createAdminRole();
        em.persist(role);
        user = UserFactory.createAdminUser(role);
        em.persist(user);
        em.flush();
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(user.getEmail());
        loginRequest.setPassword("password");
        return signinService.signIn(loginRequest);
    }

    public User getUser() {
        return user;
    }


}
