package bgpersonnel.budget.service.csv;

import java.io.ByteArrayInputStream;
import java.util.List;

public interface CsvGenerator<T> {
    public ByteArrayInputStream generateCSV(List<T> donnees);
}
