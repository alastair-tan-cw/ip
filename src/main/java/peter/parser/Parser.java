package peter.parser;

import peter.exception.PeterException;
import peter.storage.Storage;
import peter.task.*;
import peter.ui.Ui;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Parses user input and executes the corresponding commands.
 */
public class Parser {

    /**
     * Parses the user input and performs the requested action.
     *
     * @param userInput The full input string entered by the user.
     * @param tasks     The current list of tasks.
     * @param ui        The UI object to handle user interaction.
     * @param storage   The Storage object to handle loading and saving data.
     * @return true if the user input is the exit command ("bye"), false otherwise.
     */
    public static boolean parse(String userInput, TaskList tasks, Ui ui, Storage storage) {
        try {
            String[] splitStr = userInput.split(" ", 2);
            switch(splitStr[0]) {
                case "bye":
                    ui.printOutput("Goodbye. Chat again soon?");
                    return true;

                case "list":
                    if (tasks.size() == 0) {
                        ui.printOutput("Nothing in list yet.");
                        break;
                    }
                    String listStr = "Here are the tasks in your list:\n";
                    for (int i = 0; i < tasks.size(); i++) {
                        listStr += i + 1 + "." + tasks.get(i);
                        if (i != tasks.size() - 1) {
                            listStr += "\n";
                        }
                    }
                    ui.printOutput(listStr);
                    break;

                case "delete":
                    int delIndex = Integer.parseInt(splitStr[1]) - 1;
                    if (delIndex < 0 || delIndex >= tasks.size()) {
                        throw new PeterException("Invalid index. Item does not exist in list.");
                    }
                    Task deletedTask = tasks.delete(delIndex);
                    storage.saveFile(tasks.getAllTasks());
                    ui.printOutput("Noted. I've removed this task:\n" + deletedTask +
                            "\nNow you have " + tasks.size() + " tasks in your list.");
                    break;

                case "mark":
                    int markIndex = Integer.parseInt(splitStr[1]) - 1;
                    if (markIndex < 0 || markIndex >= tasks.size()) {
                        throw new PeterException("Invalid index. Item does not exist in list.");
                    }
                    tasks.get(markIndex).markAsDone();
                    storage.saveFile(tasks.getAllTasks());
                    ui.printOutput("Keep it up! I've marked this task as done:\n" + tasks.get(markIndex));
                    break;

                case "unmark":
                    int unmarkIndex = Integer.parseInt(splitStr[1]) - 1;
                    if (unmarkIndex < 0 || unmarkIndex >= tasks.size()) {
                        throw new PeterException("Invalid index. Item does not exist in list.");
                    }
                    tasks.get(unmarkIndex).markAsUndone();
                    storage.saveFile(tasks.getAllTasks());
                    ui.printOutput("Keep it up! I've unmarked this task:\n" + tasks.get(unmarkIndex));
                    break;

                case "todo":
                    if (userInput.length() <= 5) {
                        throw new PeterException("Sorry! Description of todo cannot be empty.");
                    }
                    Task todo = new Todo(splitStr[1]);
                    tasks.add(todo);
                    storage.saveFile(tasks.getAllTasks());
                    ui.printOutput(">> Got it! I've added this task:\n" + todo +
                            "\nNow you have " + tasks.size() + " tasks in your list.");
                    break;

                case "deadline":
                    if (userInput.length() <= 9) {
                        throw new PeterException("Sorry! Description of event cannot be empty.");
                    }

                    if (!splitStr[1].contains(" /by ")) {
                        throw new PeterException("Incorrect format. Should be deadline <task> /by <time>");
                    }

                    String[] dSplit = splitStr[1].split(" /by ");

                    try {
                        LocalDate date = LocalDate.parse(dSplit[1]);
                        Task deadline = new Deadline(dSplit[0], date);

                        tasks.add(deadline);
                        storage.saveFile(tasks.getAllTasks());
                        ui.printOutput(">> Got it! I've added this task:\n" +
                                deadline +
                                "\nNow you have " + tasks.size() + " tasks in your list.");

                    } catch (DateTimeParseException e) {
                        throw new PeterException("Invalid Date Format! Should be yyyy-mm-dd (e.g. 2024-01-30).");
                    }

                    break;

                case "event":
                    if (userInput.length() <= 6) {
                        throw new PeterException("Sorry! Description of event cannot be empty.");
                    }

                    if (!splitStr[1].contains(" /from ") || !splitStr[1].contains(" /to ")) {
                        throw new PeterException("Incorrect format. Should be event <task> /from <time> /to <time>");
                    }

                    // read book /from 12 June /to 14 June
                    String[] inputArr = splitStr[1].split(" /from ");
                    String description = inputArr[0];

                    String[] dateArr = inputArr[1].split(" /to ");
                    String start = dateArr[0];
                    String end = dateArr[1];

                    Task event = new Event(description, start, end);
                    tasks.add(event);
                    storage.saveFile(tasks.getAllTasks());
                    ui.printOutput(">> Got it! I've added this task:\n" +
                            event +
                            "\nNow you have " + tasks.size() + " tasks in your list.");
                    break;
                default:
                    throw new PeterException("Sorry, I do not know what that means. Would you like to add\n" +
                            "a task using 'todo', 'deadline' or 'event'?");
            }

        } catch (PeterException | DateTimeParseException | NumberFormatException | IndexOutOfBoundsException e) {
            ui.printError(e.getMessage());
        }
        return false;
    }
}