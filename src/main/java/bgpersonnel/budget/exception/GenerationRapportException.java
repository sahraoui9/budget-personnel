package bgpersonnel.budget.exception;

public class GenerationRapportException extends RuntimeException {
    public GenerationRapportException(String message) {
        super(message);
    }

    public GenerationRapportException(String message, Throwable cause) {
        super(message, cause);
    }
}
