package bgpersonnel.budget.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionReportDto {
    private String name;
    private LocalDate date;
    private double amount;
    private String description;
    private String typeTransaction;

    public TransactionReportDto(String name, LocalDate date, double amount, String description, TypeTransaction typeTransaction) {
        this.name = name;
        this.date = date;
        this.amount = amount;
        this.description = description;
        this.typeTransaction = typeTransaction.getName();
    }
}
