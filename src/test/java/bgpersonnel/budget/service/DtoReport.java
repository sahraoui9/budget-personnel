package bgpersonnel.budget.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class DtoReport {
    private String nom;
    private String prenom;
    private String email;
}
