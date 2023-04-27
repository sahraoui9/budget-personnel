package bgpersonnel.budget.reporting;


import bgpersonnel.budget.authentification.common.services.UserService;
import bgpersonnel.budget.service.csv.CsvGenerator;
import bgpersonnel.budget.service.excel.ExcelGenerator;
import bgpersonnel.budget.service.pdf.PdfGenerator;
import bgpersonnel.budget.transaction.Transaction;
import bgpersonnel.budget.transaction.TransactionRepository;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.List;

@Service
public class ReportingServiceImpl implements ReportingService {

    // csv
    private final CsvGenerator<DataReport> csvGenerator;
    // excel
    private final ExcelGenerator<DataReport> excelGenerator;
    // pdf
    private final PdfGenerator<DataReport> pdfGenerator;

    // transaction repository
    private TransactionRepository transactionRepository;

    public ReportingServiceImpl(CsvGenerator csvGenerator, ExcelGenerator excelGenerator, PdfGenerator pdfGenerator,
                                TransactionRepository transactionRepository) {
        this.csvGenerator = csvGenerator;
        this.excelGenerator = excelGenerator;
        this.pdfGenerator = pdfGenerator;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public ByteArrayInputStream generateReport(ReportRequest reportRequest) {
        List<DataReport> donnees = List.of(
                new DataReport("test", "test2"),
                new DataReport("test3", "test4")
        );
        Long userId = UserService.getIdConnectedUser();
       // List<Transaction> transactions = transactionRepository.findAllByUserBetweenTwoDatesAndByCategory(userId, reportRequest.getStartDate(), reportRequest.getEndDate(), reportRequest.getCategoryId());
        //System.out.println(transactions);
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
