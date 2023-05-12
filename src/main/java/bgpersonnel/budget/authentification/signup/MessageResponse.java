package bgpersonnel.budget.authentification.signup;

import lombok.Getter;
import lombok.Setter;

@Getter
public class MessageResponse {
    private String message;

    public MessageResponse(String message) {
        this.message = message;
    }

}