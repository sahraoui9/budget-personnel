package bgpersonnel.budget.authentification.services;

import bgpersonnel.budget.authentification.entity.User;
import bgpersonnel.budget.authentification.repository.UserRepository;
import bgpersonnel.budget.exeception.NotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User with username " + username + " not found"));
    }
}
