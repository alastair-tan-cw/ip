package peter.ui;

import peter.task.Task;


public class Ui {
    public static final String NAME = "Peter";

    public void printWelcome() {
        printOutput(" Hello! I'm " + NAME + "\n" +
                " What can I do for you?");
    }

    public void printError(String message) {
        printOutput(message);
    }

    public void showFileError() {
        printOutput("No saved tasks found. Starting with empty list.");
    }

    public void printOutput (String input) {
        System.out.println("____________________________________________________________\n" +
                input +
                "\n____________________________________________________________");
    }
}
