package bgpersonnel.budget.authentification.mangeruser;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdatedPassword {
    @NotBlank
    private String oldPassword;
    @NotBlank
    private String newPassword;
}
