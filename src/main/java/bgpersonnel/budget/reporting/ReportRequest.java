package bgpersonnel.budget.reporting;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor

public class ReportRequest {
    private LocalDate startDate;
    private LocalDate endDate;
    private ETypeReport reportType;
    private Long categoryId;
}
