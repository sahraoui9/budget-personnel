package bgpersonnel.budget.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public abstract class BaseEntity {
    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;
}
