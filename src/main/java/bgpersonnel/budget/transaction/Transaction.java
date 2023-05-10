package bgpersonnel.budget.transaction;


import bgpersonnel.budget.authentification.common.entity.User;
import bgpersonnel.budget.category.Category;
import bgpersonnel.budget.model.BaseEntity;
import bgpersonnel.budget.objectif.Objectif;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Transaction extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private double amount;
    private String description;
    private LocalDate dateTransaction;
    private TypeTransaction typeTransaction;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "objectif_id")
    private Objectif objectif;

}
