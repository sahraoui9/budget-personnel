package bgpersonnel.budget.fixtures;

import bgpersonnel.budget.authentification.common.entity.ERole;
import bgpersonnel.budget.authentification.common.entity.Role;
import bgpersonnel.budget.authentification.common.entity.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContexts;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
@Profile("test")

public class UserFactory {

    public static User createAdminUser(Role role) {

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user = new User();
        user.setEmail("admin@mail.com");
        user.setName("admin");
        user.setPassword(passwordEncoder.encode("password"));
        user.getRoles().add(role);
        return user;
    }

    // create role admin
    public static Role createAdminRole() {
        Role role = new Role();
        role.setName(ERole.ROLE_ADMIN);
        return role;
    }
}
