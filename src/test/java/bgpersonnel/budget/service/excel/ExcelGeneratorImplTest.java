package bgpersonnel.budget.service.excel;


import bgpersonnel.budget.service.DtoReport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;


@ExtendWith(MockitoExtension.class)
class ExcelGeneratorImplTest {

    private ExcelGeneratorImpl<DtoReport> excelGenerator;

    @BeforeEach
    void setUp() {
        excelGenerator = new ExcelGeneratorImpl<>();
    }


    @Test
    @DisplayName("Test generateExcel avec des données non valides")
    void testGenerateExcel() {
        List<DtoReport> data = new ArrayList<>();
        data.add(new DtoReport("John", "Doe", "test@mail"));
        data.add(new DtoReport("Jane", "Max", "test1@mail"));
        String[] headers = {"Nom", "Prénom", "Email"};
        ByteArrayInputStream byteArrayInputStream = excelGenerator.generateExcel(data, headers);
        Assertions.assertNotNull(byteArrayInputStream);
    }


}
