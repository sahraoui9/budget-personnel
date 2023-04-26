package bgpersonnel.budget.service.csv;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.List;

@Service
@Slf4j
public class CsvGeneratorImpl<T> implements CsvGenerator<T> {
    public ByteArrayInputStream generateCSV(List<T> donnees) {

        // Créer le contenu du rapport CSV en utilisant OpenCSV
        StringWriter writer = new StringWriter();
        StatefulBeanToCsv<T> beanToCsv = new StatefulBeanToCsvBuilder<T>(writer)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withOrderedResults(false)
                .build();
        try {
            beanToCsv.write(donnees);
            writer.flush();
            return new ByteArrayInputStream(writer.toString().getBytes());
        } catch (CsvException e) {
            // Gérer les exceptions
            log.error("Erreur lors de la génération du fichier CSV", e);
            return null;
        }

    }

}
