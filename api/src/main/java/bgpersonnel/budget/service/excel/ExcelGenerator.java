package bgpersonnel.budget.service.excel;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public interface ExcelGenerator<T> {
    public ByteArrayInputStream generateExcel(List<T> data, String[] headers);
}
