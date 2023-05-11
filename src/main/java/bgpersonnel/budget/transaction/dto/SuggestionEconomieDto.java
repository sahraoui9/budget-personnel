package bgpersonnel.budget.transaction.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class SuggestionEconomieDto {

    private String name;
    private double depense;
    private double depenseTotal;
    private double percentage;
    private double percentagePerCategory;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
}
