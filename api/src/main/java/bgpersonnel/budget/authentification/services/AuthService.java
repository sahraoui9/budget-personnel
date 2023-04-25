package bgpersonnel.budget.authentification.services;
import bgpersonnel.budget.authentification.entity.ERole;
import bgpersonnel.budget.authentification.entity.RefreshToken;
import bgpersonnel.budget.authentification.entity.Role;
import bgpersonnel.budget.authentification.entity.User;
import bgpersonnel.budget.authentification.payload.request.LoginRequest;
import bgpersonnel.budget.authentification.payload.request.SignupRequest;
import bgpersonnel.budget.authentification.payload.response.JwtResponse;
import bgpersonnel.budget.authentification.repository.RoleRepository;
import bgpersonnel.budget.authentification.repository.UserRepository;
import bgpersonnel.budget.authentification.security.JwtUtils;
import bgpersonnel.budget.authentification.security.services.RefreshTokenService;
import bgpersonnel.budget.authentification.security.services.UserDetailsImpl;
import bgpersonnel.budget.exeception.AlreadyExistsException;
import bgpersonnel.budget.exeception.NotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class AuthService {

    AuthenticationManager authenticationManager;
    UserRepository userRepository;
    RoleRepository roleRepository;
    PasswordEncoder encoder;
    JwtUtils jwtUtils;
    RefreshTokenService refreshTokenService;

    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository,
                       PasswordEncoder encoder, JwtUtils jwtUtils, RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.refreshTokenService = refreshTokenService;
    }

    public JwtResponse signIn(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwt = jwtUtils.generateJwtToken(userDetails);

        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                .toList();

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        return new JwtResponse(jwt, refreshToken.getToken(), userDetails.getId(),
                userDetails.getUsername(), userDetails.getEmail(), roles);
    }

    // optimize this method
    public void registerUser(SignupRequest signUpRequest) {
        validateUsernameAndPassword(signUpRequest);
        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<Role> roles = getRoles(signUpRequest);

        user.setRoles(roles);
        userRepository.save(user);
    }

    private Set<Role> getRoles(SignupRequest signUpRequest) {
        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            roles.add(findRoleByNameOrThrow(ERole.ROLE_USER));
            return roles;
        }

        strRoles.forEach(role -> {
            switch (role) {
                case "admin" -> roles.add(findRoleByNameOrThrow(ERole.ROLE_ADMIN));
                case "mod" -> roles.add(findRoleByNameOrThrow(ERole.ROLE_MODERATOR));
                default -> roles.add(findRoleByNameOrThrow(ERole.ROLE_USER));
            }
        });

        return roles;
    }

    private void validateUsernameAndPassword(SignupRequest signUpRequest) {
        if (Boolean.TRUE.equals(userRepository.existsByUsername(signUpRequest.getUsername()))) {
            throw new AlreadyExistsException("Error: Username is already taken!");
        }
        if (Boolean.TRUE.equals(userRepository.existsByEmail(signUpRequest.getEmail()))) {
            throw new AlreadyExistsException("Error: Email is already in use!");
        }
    }


    private Role findRoleByNameOrThrow(ERole roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new NotFoundException("Error: Role is not found."));
    }

}
