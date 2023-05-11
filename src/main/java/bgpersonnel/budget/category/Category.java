package bgpersonnel.budget.category;

import bgpersonnel.budget.authentification.common.entity.User;
import bgpersonnel.budget.budget.Budget;
import bgpersonnel.budget.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @OneToOne(mappedBy = "category")
    private Budget budget;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Category(Long id) {
        this.id = id;
    }
}
