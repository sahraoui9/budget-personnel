package bgpersonnel.budget;


import bgpersonnel.budget.entity.ERole;
import bgpersonnel.budget.entity.Role;
import bgpersonnel.budget.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
public class InitDb implements CommandLineRunner {
    @PersistenceContext
    private EntityManager entityManager;

     PasswordEncoder encoder;

    public InitDb(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        Role roleUser = new Role();
        roleUser.setName(ERole.ROLE_USER);
        entityManager.persist(roleUser);

        Role roleAdmin = new Role();
        roleAdmin.setName(ERole.ROLE_ADMIN);
        entityManager.persist(roleAdmin);

        Role roleModerator = new Role();
        roleModerator.setName(ERole.ROLE_MODERATOR);
        entityManager.persist(roleModerator);

        // Users
        String password = encoder.encode("password");

        User user1 = new User();
        user1.setUsername("user1");
        user1.setEmail("user@mail.com");
        user1.setPassword(password);
        user1.getRoles().add(roleUser);
        entityManager.persist(user1);



        entityManager.flush();
    }
}
