package bgpersonnel.budget.reporting.depense;

import bgpersonnel.budget.reporting.ETypeReport;

import java.io.ByteArrayInputStream;

public interface ExpenseReport {
    /**
     * Rapport liste de catégorie ou les dépenses sont trop importantes par rapport aux autres catégories
     *
     * @return ByteArrayInputStream
     */
    ByteArrayInputStream generateReportCategoryTooExpensive(ETypeReport type);
}
