package bgpersonnel.budget.service.csv;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.List;

@Service
@Slf4j
public class CsvGeneratorImpl<T> implements CsvGenerator<T> {
    public ByteArrayInputStream generateCSV(List<T> donnees, String[] header) {

        // Créer le contenu du rapport CSV en utilisant OpenCSV
        StringWriter writer = new StringWriter();

        CSVWriter csvWriter = new CSVWriter(writer, CSVWriter.DEFAULT_SEPARATOR,
                CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END);

        csvWriter.writeNext(header);

        // Écrire les données sans inclure le header automatique skip les noms des colonnes
        StatefulBeanToCsv<T> beanToCsv = new StatefulBeanToCsvBuilder<T>(csvWriter)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withOrderedResults(false)
                .build();

        try {
            beanToCsv.write(donnees);
            csvWriter.flush();
            return new ByteArrayInputStream(writer.toString().getBytes());
        } catch (Exception e) {
            // Gérer les exceptions
            log.error("Erreur lors de la génération du fichier CSV", e);
            return null;
        }

    }


}
