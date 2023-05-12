package bgpersonnel.budget.reporting.depense;

import bgpersonnel.budget.exeception.TypeRapportNotFoundException;
import bgpersonnel.budget.reporting.ETypeReport;
import bgpersonnel.budget.service.csv.CsvGenerator;
import bgpersonnel.budget.service.excel.ExcelGenerator;
import bgpersonnel.budget.service.pdf.PdfGenerator;
import bgpersonnel.budget.transaction.TransactionService;
import bgpersonnel.budget.transaction.dto.SuggestionEconomieDto;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.List;

@Service
class ExpenseReportImpl implements ExpenseReport {
    private final CsvGenerator<SuggestionEconomieDto> csvGenerator;
    private final ExcelGenerator<SuggestionEconomieDto> excelGenerator;
    private final PdfGenerator<SuggestionEconomieDto> pdfGenerator;

    private final TransactionService transactionService;

    public ExpenseReportImpl(CsvGenerator<SuggestionEconomieDto> csvGenerator,
                             ExcelGenerator<SuggestionEconomieDto> excelGenerator,
                             PdfGenerator<SuggestionEconomieDto> pdfGenerator,
                             TransactionService transactionService) {
        this.csvGenerator = csvGenerator;
        this.excelGenerator = excelGenerator;
        this.pdfGenerator = pdfGenerator;
        this.transactionService = transactionService;
    }

    @Override
    public ByteArrayInputStream generateReportCategoryTooExpensive(ETypeReport type) {


        List<SuggestionEconomieDto> transactions = transactionService.getSuggestionEconomie();
        String[] header = {"Nom", "Dépense", "Dépense total", "Pourcentage", "Pourcentage par catégorie", "Date de début", "Date de fin"};
        return switch (type) {
            case CSV -> csvGenerator.generateCSV(transactions, header);
            case XLS -> excelGenerator.generateExcel(transactions, header);
            case PDF -> pdfGenerator.generatePdf(transactions, "/report_depense.html", header);
            default -> throw new TypeRapportNotFoundException("Le type de rapport demandé n'existe pas" + type);
        };

    }
}
