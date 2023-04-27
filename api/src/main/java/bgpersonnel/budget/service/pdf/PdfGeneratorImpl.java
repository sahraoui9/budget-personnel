package bgpersonnel.budget.service.pdf;


import bgpersonnel.budget.service.GeneratorHelper;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.util.List;

;


@Service
@Slf4j
public class PdfGeneratorImpl<T> implements PdfGenerator<T> {
    @Override
    public ByteArrayInputStream generatePdf(List<T> data, String[] headers, String html) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Générer le contenu HTML du PDF avec le titre et le tableau
        String htmlContent = "<h1 style='color:red'> List </h1>" +
                "<table>";
        for (String header : headers) {
            htmlContent += "<th>" + header + "</th>";
        }

        for (T t : data) {
            htmlContent += "<tr>";
            for (Field field : t.getClass().getDeclaredFields()) {
                String fieldName = field.getName();
                // Get the value of the field from the object record
                Object fieldValue = GeneratorHelper.getFieldValueFromObject(t, fieldName);
                htmlContent += "<td>" + (fieldValue != null ? String.valueOf(fieldValue) : "") + "</td>";
            }
            htmlContent += "</tr>";
        }
        htmlContent += "</table>";

        ConverterProperties converterProperties = new ConverterProperties();
        HtmlConverter.convertToPdf(htmlContent, outputStream, converterProperties);

        return new ByteArrayInputStream(outputStream.toByteArray());

    }


}
