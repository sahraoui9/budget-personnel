package bgpersonnel.budget.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.*;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BaseEntity {
    @Column(name = "createdBy")
    private String createdBy;
    @Column(name = "createdAt")
    private LocalDateTime createdAt;
    @Column(name = "updatedBy")
    private String updatedBy;
    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;
}
