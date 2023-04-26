package bgpersonnel.budget.reporting;

import bgpersonnel.budget.service.csv.CsvGenerator;
import bgpersonnel.budget.service.excel.ExcelGenerator;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.ByteArrayInputStream;
import java.util.List;

@Service
public class ReportingServiceImpl implements ReportingService {

    // csv
    private CsvGenerator<DataReport> csvGenerator;
    // excel
    private ExcelGenerator<DataReport> excelGenerator;

    public ReportingServiceImpl(CsvGenerator csvGenerator, ExcelGenerator excelGenerator) {
        this.csvGenerator = csvGenerator;
        this.excelGenerator = excelGenerator;
    }

    @Override
    public ByteArrayInputStream generateReport(ReportRequest reportRequest) {
          List<DataReport> donnees = List.of(
                    new DataReport("test", "test2"),
                    new DataReport("test3", "test4")
            );
        if (reportRequest.getReportType().equals(ETypeReport.CSV)) {
            return csvGenerator.generateCSV(donnees);
        }
        if (reportRequest.getReportType().equals(ETypeReport.XLS)) {
            return excelGenerator.generateExcel(donnees, new String[]{"test", "test2"});
        }
        return null;
    }


}
