package bgpersonnel.budget.services;

import bgpersonnel.budget.entity.User;
import bgpersonnel.budget.exeception.NotFoundException;
import bgpersonnel.budget.repository.UserRepository;
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
