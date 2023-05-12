package bgpersonnel.budget.reporting.depense;

import bgpersonnel.budget.reporting.ETypeReport;
import bgpersonnel.budget.service.csv.CsvGenerator;
import bgpersonnel.budget.service.excel.ExcelGenerator;
import bgpersonnel.budget.service.pdf.PdfGenerator;
import bgpersonnel.budget.transaction.TransactionService;
import bgpersonnel.budget.transaction.dto.SuggestionEconomieDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExpenseReportImplTest {

    private CsvGenerator<SuggestionEconomieDto> csvGenerator;
    private ExcelGenerator<SuggestionEconomieDto> excelGenerator;
    private PdfGenerator<SuggestionEconomieDto> pdfGenerator;
    private TransactionService transactionService;
    private ExpenseReportImpl expenseReport;

    private static final String[] HEADER = new String[]{"Nom", "Dépense", "Dépense total", "Pourcentage", "Pourcentage par catégorie", "Date de début", "Date de fin"};

    @BeforeEach
    void setUp() {
        csvGenerator = mock(CsvGenerator.class);
        excelGenerator = mock(ExcelGenerator.class);
        pdfGenerator = mock(PdfGenerator.class);
        transactionService = mock(TransactionService.class);
        expenseReport = new ExpenseReportImpl(csvGenerator, excelGenerator, pdfGenerator, transactionService);
    }

    @Test
    void generateReportCategoryTooExpensive_csv() {
        // given
        List<SuggestionEconomieDto> transactions = new ArrayList<>();
        when(transactionService.getSuggestionEconomie()).thenReturn(transactions);
        byte[] expectedBytes = "csv report content".getBytes();
        when(csvGenerator.generateCSV(transactions, HEADER)).thenReturn(new ByteArrayInputStream(expectedBytes));

        // when
        ByteArrayInputStream report = expenseReport.generateReportCategoryTooExpensive(ETypeReport.CSV);

        // then
        assertNotNull(report);
        assertEquals(expectedBytes.length, report.available());
    }

    @Test
    void generateReportCategoryTooExpensive_xls() {
        // given
        List<SuggestionEconomieDto> transactions = new ArrayList<>();
        when(transactionService.getSuggestionEconomie()).thenReturn(transactions);
        byte[] expectedBytes = "xls report content".getBytes();
        when(excelGenerator.generateExcel(transactions, HEADER)).thenReturn(new ByteArrayInputStream(expectedBytes));

        // when
        ByteArrayInputStream report = expenseReport.generateReportCategoryTooExpensive(ETypeReport.XLS);

        // then
        assertNotNull(report);
        assertEquals(expectedBytes.length, report.available());
    }

    @Test
    void generateReportCategoryTooExpensive_pdf() {
        // given
        List<SuggestionEconomieDto> transactions = new ArrayList<>();
        when(transactionService.getSuggestionEconomie()).thenReturn(transactions);
        byte[] expectedBytes = "pdf report content".getBytes();
        when(pdfGenerator.generatePdf(transactions, "/report_depense.html", HEADER)).thenReturn(new ByteArrayInputStream(expectedBytes));

        // when
        ByteArrayInputStream report = expenseReport.generateReportCategoryTooExpensive(ETypeReport.PDF);

        // then
        assertNotNull(report);
        assertEquals(expectedBytes.length, report.available());
    }

}