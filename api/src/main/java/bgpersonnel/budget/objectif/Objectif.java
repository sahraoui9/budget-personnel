package bgpersonnel.budget.objectif;

import bgpersonnel.budget.authentification.entity.User;
import bgpersonnel.budget.category.Category;
import bgpersonnel.budget.model.BaseEntity;
import bgpersonnel.budget.transaction.Transaction;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
public class Objectif extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private double amount;

    private String description;

    private LocalDateTime term;

    private boolean isReached;

    @OneToMany(mappedBy = "objectif")
    private List<Transaction> transactions = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;
}