package bgpersonnel.budget.category;

import bgpersonnel.budget.budget.Budget;
import bgpersonnel.budget.model.BaseEntity;
import bgpersonnel.budget.transaction.Transaction;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "category")
    private List<Transaction> transactions = new ArrayList<>();

    @OneToOne(mappedBy = "category")
    private Budget budget;
}
