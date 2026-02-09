import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Peter {
    public static final String NAME = "Peter";
    public static final String FILE_PATH = "./data/storagetest.txt";
    private static Storage storage = new Storage(FILE_PATH);
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

    public static void printOutput (String input) {
        System.out.println("____________________________________________________________\n" +
                input +
                "\n____________________________________________________________");
    }

    public static void addTask(String userInput, List<Task> list) throws PeterException {
        if (userInput.startsWith("todo")) {
            if (userInput.length() <= 5) {
                throw new PeterException("Sorry! Description of todo cannot be empty.");
            }

            userInput = userInput.substring(5);
            Task todo = new Todo(userInput);
            list.add(todo);
            storage.saveFile(list);
            printOutput(">> Got it! I've added this task:\n" +
                    todo +
                    "\nNow you have " + list.size() + " tasks in your list.");

        }

        if (userInput.startsWith("deadline")) {
            if (userInput.length() <= 9) {
                throw new PeterException("Sorry! Description of deadline cannot be empty.");
            }

            userInput = userInput.substring(9);

            if (!userInput.contains(" /by ")) {
                throw new PeterException("Incorrect format. Should be deadline <task> /by <time>");
            }

            String[] inputArr = userInput.split(" /by ");

            try {
                LocalDate date = LocalDate.parse(inputArr[1]);
                Task deadline = new Deadline(inputArr[0], date);

                list.add(deadline);
                storage.saveFile(list);
                printOutput(">> Got it! I've added this task:\n" +
                        deadline +
                        "\nNow you have " + list.size() + " tasks in your list.");

            } catch (DateTimeParseException e) {
                throw new PeterException("Invalid Date Format! Should be yyyy-mm-dd (e.g. 2024-01-30).");
            }
        }

        if (userInput.startsWith("event")) {
            if (userInput.length() <= 6) {
                throw new PeterException("Sorry! Description of event cannot be empty.");
            }

            userInput = userInput.substring(6);

            if (!userInput.contains(" /from ") || !userInput.contains(" /to ")) {
                throw new PeterException("Incorrect format. Should be event <task> /from <time> /to <time>");
            }

            // read book /from 12 June /to 14 June
            String[] inputArr = userInput.split(" /from ");
            String description = inputArr[0];

            String[] dateArr = inputArr[1].split(" /to ");
            String start = dateArr[0];
            String end = dateArr[1];

            Task event = new Event(description, start, end);
            list.add(event);
            storage.saveFile(list);
            printOutput(">> Got it! I've added this task:\n" +
                    event +
                    "\nNow you have " + list.size() + " tasks in your list.");
        }
    }

    public static void markTask(String userInput, List<Task> list) throws PeterException {
        if (userInput.startsWith("mark") || userInput.startsWith("unmark")) {
            // then mark the right item
            String[] splitStr = userInput.split(" ");

            int markIndex;

            try {
                markIndex = Integer.parseInt(splitStr[1]) - 1;
            } catch (NumberFormatException e) {
                throw new PeterException("Index must be a number.");
            }

            if (markIndex < 0 || markIndex >= list.size()) {
                throw new PeterException("Invalid index. Item does not exist in list.");
            }

            Task task = list.get(markIndex);

            if (userInput.startsWith("mark")) {
                task.markAsDone();
                storage.saveFile(list);
                printOutput("Keep it up! I've marked this task as done:\n" +
                        task);
            } else {
                task.markAsUndone();
                storage.saveFile(list);
                printOutput("Got it. I've marked this task as not done yet:\n" +
                        task);
            }
        }
    }

    public static void deleteTask(String userInput, List<Task> list) throws PeterException {
        String[] splitStr = userInput.split(" ");

        int delIndex;

        try {
            delIndex = Integer.parseInt(splitStr[1]) - 1;
        } catch (NumberFormatException e) {
            throw new PeterException("Index must be a number.");
        }

        if (delIndex < 0 || delIndex >= list.size()) {
            throw new PeterException("Invalid index. Item does not exist in list.");
        }

        Task task = list.remove(delIndex);
        storage.saveFile(list);
        printOutput("Noted. I've removed this task:\n" +
                task +
                "\nNow you have " + list.size() + " tasks in your list.");
    }

    public static void main(String[] args) {
        List<Task> list = new ArrayList<>();

        try {
            list = storage.loadFile();
        } catch (PeterException e) {
            printOutput("Error loading file.");
        }
        String menu = " Hello! I'm " + NAME + "\n" +
                " What can I do for you?";

        printOutput(menu);

        Scanner sc = new Scanner(System.in);
        while (true) {
            String userInput = sc.nextLine();

            if (userInput.equals("bye")) {
                printOutput("Goodbye. Chat again soon?");
                break;
            }

            if (userInput.equals("list")) {
                if (list.isEmpty()) {
                    printOutput("Nothing in list yet.");
                    continue;
                }

                String listStr = "Here are the tasks in your list:\n";
                for (int i = 0; i < list.size(); i++) {
                    listStr += i + 1 + "." + list.get(i);
                    if (i != list.size() - 1) {
                        listStr += "\n";
                    }
                }
                printOutput(listStr);
                continue;
            }

            try {
                if (userInput.startsWith("delete")) {
                    deleteTask(userInput, list);
                } else if (userInput.startsWith("mark") || userInput.startsWith("unmark")) {
                    markTask(userInput, list);
                } else if (userInput.startsWith("todo") || userInput.startsWith("deadline") || userInput.startsWith("event")) {
                    addTask(userInput, list);
                } else {
                    printOutput("Sorry, I do not know what that means. Would you like to add\n" +
                            "a task using 'todo', 'deadline' or 'event'?");
                }
            } catch (Exception e) {
                printOutput(e.getMessage());
            }
        }
    }
}
