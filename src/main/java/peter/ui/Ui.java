package peter.ui;

/**
 * Handles all user interactions, such as reading input and printing output.
 */
public class Ui {
    public static final String NAME = "Peter";

    /**
     * Displays the welcome message to the user upon startup.
     */
    public void printWelcome() {
        printOutput(" Hello! I'm " + NAME + "\n" +
                " What can I do for you?");
    }

    /**
     * Displays an error message to the user.
     *
     * @param message The error message to be displayed.
     */
    public void printError(String message) {
        printOutput(message);
    }

    /**
     * Displays a message when file does not exist or no saved tasks were found.
     */
    public void showFileError() {
        printOutput("No saved tasks found. Starting with empty list.");
    }

    /**
     * Prints a message in terminal, enclosed in horizontal lines above and below the message.
     *
     * @param input The string message to print.
     */
    public void printOutput (String input) {
        System.out.println("____________________________________________________________\n" +
                input +
                "\n____________________________________________________________");
    }
}
