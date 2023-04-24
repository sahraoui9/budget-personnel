package bgpersonnel.budget.controller;


import bgpersonnel.budget.entity.RefreshToken;
import bgpersonnel.budget.payload.request.LoginRequest;
import bgpersonnel.budget.payload.request.SignupRequest;
import bgpersonnel.budget.payload.request.TokenRefreshRequest;
import bgpersonnel.budget.payload.response.JwtResponse;
import bgpersonnel.budget.payload.response.MessageResponse;
import bgpersonnel.budget.payload.response.TokenRefreshResponse;
import bgpersonnel.budget.security.JwtUtils;
import bgpersonnel.budget.security.exeception.TokenRefreshException;
import bgpersonnel.budget.security.services.RefreshTokenService;
import bgpersonnel.budget.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthService authService;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    RefreshTokenService refreshTokenService;

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.signIn(loginRequest));
    }


    @PostMapping("/refreshtoken")
    public ResponseEntity<TokenRefreshResponse> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getUsername());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token is not in database!"));
    }

    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        authService.registerUser(signUpRequest);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}