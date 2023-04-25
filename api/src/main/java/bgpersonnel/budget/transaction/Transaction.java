package bgpersonnel.budget.transaction;

import bgpersonnel.budget.authentification.entity.User;
import bgpersonnel.budget.category.Category;
import bgpersonnel.budget.model.BaseEntity;
import bgpersonnel.budget.objectif.Objectif;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Transaction extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private double amount;
    private String description;
    private LocalDateTime dateTransaction;
    private TypeTransaction typeTransaction;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "transaction_id")
    private Objectif objectif;

}
