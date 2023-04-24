package bgpersonnel.budget.objectif;

import bgpersonnel.budget.authentification.entity.User;
import bgpersonnel.budget.category.Category;
import bgpersonnel.budget.model.BaseEntity;
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
    private double amount;
    private String description;

    private LocalDateTime term;

    private boolean isReached;

    private TypeObjectif typeObjectif;

    @ManyToMany(mappedBy = "objectifs")
    private List <Category> categories = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;
}
