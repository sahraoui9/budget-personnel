package bgpersonnel.budget.authentification.signin;


import lombok.Getter;

import java.util.List;

@Getter
public class JwtResponse {
    private final String token;
    private static final String BEARER = "Bearer";
    private final String refreshToken;
    private final Long id;
    private final String name;
    private final String email;
    private final List<String> roles;

    public JwtResponse(String accessToken, String refreshToken, Long id, String name, String email, List<String> roles) {
        this.token = accessToken;
        this.refreshToken = refreshToken;
        this.id = id;
        this.name = name;
        this.email = email;
        this.roles = roles;
    }

}