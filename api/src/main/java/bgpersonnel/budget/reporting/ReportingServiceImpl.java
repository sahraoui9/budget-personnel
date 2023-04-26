package bgpersonnel.budget.reporting;

import bgpersonnel.budget.service.csv.CsvGenerator;
import bgpersonnel.budget.service.excel.ExcelGenerator;
import bgpersonnel.budget.service.pdf.PdfGenerator;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.List;

@Service
public class ReportingServiceImpl implements ReportingService {

    // csv
    private CsvGenerator<DataReport> csvGenerator;
    // excel
    private ExcelGenerator<DataReport> excelGenerator;
    // pdf
    private PdfGenerator<DataReport> pdfGenerator;

    public ReportingServiceImpl(CsvGenerator csvGenerator, ExcelGenerator excelGenerator, PdfGenerator pdfGenerator) {
        this.csvGenerator = csvGenerator;
        this.excelGenerator = excelGenerator;
        this.pdfGenerator = pdfGenerator;
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
        if (reportRequest.getReportType().equals(ETypeReport.PDF)) {
            return pdfGenerator.generatePdf(donnees, new String[]{"test", "test2"}, "<h1>test</h1>");
        }
        return null;
    }


}
