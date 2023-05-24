package bgpersonnel.budget.service.csv;

import bgpersonnel.budget.exception.GenerationRapportException;
import bgpersonnel.budget.service.DtoReport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CsvGeneratorImplTest {

    private CsvGeneratorImpl<DtoReport> csvGenerator;
    private List<DtoReport> donnees;

    @BeforeEach
    void setUp() {
        this.donnees = new ArrayList<>();
        csvGenerator = new CsvGeneratorImpl<DtoReport>();
    }


    @Test
    @DisplayName("Test de génération d'un rapport CSV à partir d'une liste de chaînes de caractères")
    void testGenerateCSV() {
        // Préparer les données de test
        this.donnees = List.of(
                new DtoReport("John", "Doe", "johndoe@gmail.com"),
                new DtoReport("Jane", "Doe", "janedoe@gmail.com")
        );
        String[] header = new String[]{"Prénom", "Nom", "Adresse e-mail"};

        // Générer le rapport CSV
        ByteArrayInputStream inputStream = csvGenerator.generateCSV(donnees, header);

        // Vérifier que le rapport CSV a été généré correctement
        byte[] bytes = inputStream.readAllBytes();
        String csvString = new String(bytes);
        String expectedCsvString = "Prénom,Nom,Adresse e-mail\nEMAIL,NOM,PRENOM\njohndoe@gmail.com,John,Doe\njanedoe@gmail.com,Jane,Doe\n";

        assertEquals(expectedCsvString, csvString);
    }

    @Test
    @DisplayName("Test de génération d'un rapport CSV avec une liste vide")
    void testGenerateCSVWithEmptyList() {
        // Préparer les données de test
        String[] header = new String[]{"Prénom", "Nom", "Adresse e-mail"};

        // Générer le rapport CSV
        ByteArrayInputStream inputStream = csvGenerator.generateCSV(donnees, header);

        // Vérifier que le rapport CSV a été généré correctement
        byte[] bytes = inputStream.readAllBytes();
        String csvString = new String(bytes);
        String expectedCsvString = "Prénom,Nom,Adresse e-mail\n";
        assertEquals(expectedCsvString, csvString);
    }


    @Test
    @DisplayName("Test de génération d'un rapport CSV avec des données et un header de longueur différente")
    void testGenerateCSVWithHeaderAndDataOfDifferentLength() {
        // Préparer les données de test
        this.donnees = List.of(
                new DtoReport("John", "Doe", "johndoe@gmail.com"),
                new DtoReport("Jane", "Doe", "janedoe@gmail.com")
        );
        String[] headerNotValidStrings = new String[]{"Prénom", "Nom"};

        // Vérifier que la méthode lance une exception
        assertThrows(GenerationRapportException.class, () -> {
            csvGenerator.generateCSV(donnees, headerNotValidStrings);
        });
    }
}
