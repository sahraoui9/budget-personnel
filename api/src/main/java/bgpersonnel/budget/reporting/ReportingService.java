package bgpersonnel.budget.reporting;

import java.io.ByteArrayInputStream;

public interface ReportingService {
    public ByteArrayInputStream generateReport(ReportRequest reportRequest);

}
