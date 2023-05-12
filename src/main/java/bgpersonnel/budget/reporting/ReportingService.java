package bgpersonnel.budget.reporting;

import bgpersonnel.budget.exeception.TypeRapportNotFoundException;
import bgpersonnel.budget.reporting.ReportRequest;

import java.io.ByteArrayInputStream;

public interface ReportingService {
    /**
     * Cette méthode permet de générer un rapport de liste de transactions en fonction du type de rapport demandé
     *
     * @param reportRequest ReportRequest
     * @return ByteArrayInputStream
     */
    ByteArrayInputStream generateReport(ReportRequest reportRequest) throws TypeRapportNotFoundException;

}
