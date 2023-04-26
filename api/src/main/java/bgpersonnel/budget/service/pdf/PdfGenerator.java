package bgpersonnel.budget.service.pdf;

import java.io.ByteArrayInputStream;
import java.util.List;

public interface PdfGenerator<T> {
    public ByteArrayInputStream generatePdf(List<T> data, String[] headers,String html);
}
