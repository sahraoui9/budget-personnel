package bgpersonnel.budget.authentification.services;

import bgpersonnel.budget.authentification.entity.User;
import bgpersonnel.budget.authentification.repository.UserRepository;
import bgpersonnel.budget.exeception.NotFoundException;
import bgpersonnel.budget.transaction.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findUserByUsername(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User with email " + email + " not found"));
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
