import peter.exception.PeterException;
import peter.parser.Parser;
import peter.storage.Storage;
import peter.task.TaskList;
import peter.ui.Ui;

import java.util.Scanner;

public class Peter {
    private Storage storage;
    private TaskList tasks;
    private Ui ui;

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

    public void run() {
        Scanner sc = new Scanner(System.in);
        ui.printWelcome();
        boolean isExit = false;

        while (!isExit) {
            String userInput = sc.nextLine();
            isExit = Parser.parse(userInput, tasks, ui, storage);
        }
    }


    public static void main(String[] args) {
        new Peter("./data/peter.txt").run();
    }
}
