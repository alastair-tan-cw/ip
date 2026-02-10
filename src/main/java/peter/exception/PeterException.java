package peter.exception;

/**
 * Represents exceptions specific to the Peter chatbot application.
 * This is thrown when the user inputs invalid commands or data.
 */
public class PeterException extends Exception {

    /**
     * Creates a new PeterException with the specified error message.
     *
     * @param message The error message explaining why the exception occurred.
     */
    public PeterException(String message) {
        super(message);
    }
}