package peter;

import peter.exception.PeterException;
import peter.parser.Parser;
import peter.storage.Storage;
import peter.task.TaskList;
import peter.ui.Ui;

import java.util.Scanner;

/**
 * The main entry of the peter.Peter chatbot application.
 * Initializes the UI, Storage, and TaskList, and runs the main input loop.
 */
public class Peter {
    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    /**
     * Initializes the application with the specified file path for storage.
     * Attempts to load existing data from the file; if file does not exist or is corrupted, starts with an empty list.
     *
     * @param filePath The file path where task data is stored.
     */
    public Peter(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.loadFile());
        } catch (PeterException e) {
            ui.showFileError();
            tasks = new TaskList();
        }
    }

    /**
     * Runs the main program loop.
     * Reads user inputs and executes them until the "bye" command is received.
     */
    public void run() {
        Scanner sc = new Scanner(System.in);
        ui.printWelcome();
        boolean isExit = false;

        while (!isExit) {
            String userInput = sc.nextLine();
            isExit = Parser.parse(userInput, tasks, ui, storage);
        }
    }

    /**
     * The main method that starts the application.
     *
     * @param args Command line arguments (unused).
     */
    public static void main(String[] args) {
        new Peter("./data/peter.txt").run();
    }
}
