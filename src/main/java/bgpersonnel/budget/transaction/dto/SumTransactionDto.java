package bgpersonnel.budget.transaction.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SumTransactionDto {

    private String name;

    private double sumDepense;

    private double sumRevenue;

    private double total;

    private int nbTransactions;

    private String dateDebut;

    private String dateFin;
}
