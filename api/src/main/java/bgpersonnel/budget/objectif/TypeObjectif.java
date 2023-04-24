package bgpersonnel.budget.objectif;

public enum TypeObjectif {
    ANNUEL("Annuel"),
    MENSUEL("Mensuel"),
    UNIQUE("Unique");

    private final String name;

    TypeObjectif(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
