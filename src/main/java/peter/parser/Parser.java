package peter.parser;

import peter.exception.PeterException;
import peter.storage.Storage;
import peter.task.Deadline;
import peter.task.Event;
import peter.task.Task;
import peter.task.Todo;
import peter.task.TaskList;
import peter.ui.Ui;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Parses user input and executes the corresponding commands.
 */
public class Parser {

    /**
     * Parses the user input and performs the requested action for CLI.
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
            String args = splitStr.length > 1 ? splitStr[1] : "";

            switch(splitStr[0]) {
            case "bye":
                ui.printOutput("Goodbye. Chat again soon?");
                return true;

            case "list":
                ui.printOutput(listTasks(tasks));
                break;

            case "delete":
                ui.printOutput(deleteTask(args, tasks, storage));
                break;

            case "mark":
                ui.printOutput(markTask(args, tasks, storage));
                break;

            case "unmark":
                ui.printOutput(unmarkTask(args, tasks, storage));
                break;

            case "todo":
                ui.printOutput(addTodo(args, tasks, storage));
                break;

            case "deadline":
                ui.printOutput(addDeadline(args, tasks, storage));
                break;

            case "event":
                ui.printOutput(addEvent(args, tasks, storage));
                break;

            case "find":
                ui.printOutput(findTasks(args, tasks));
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

    /**
     * Parses the user input and performs the requested action for GUI.
     *
     * @param userInput The full input string entered by the user.
     * @param tasks     The current list of tasks.
     * @param storage   The Storage object to handle loading and saving data.
     * @return true if the user input is the exit command ("bye"), false otherwise.
     */
    public static String parseGui(String userInput, TaskList tasks, Storage storage) {
        try {
            String[] splitStr = userInput.split(" ", 2);
            String args = splitStr.length > 1 ? splitStr[1] : "";
            switch(splitStr[0]) {
            case "bye":
                return "Goodbye. Chat again soon?";

            case "list":
                return listTasks(tasks);

            case "delete":
                return deleteTask(args, tasks, storage);

            case "mark":
                return markTask(args, tasks, storage);

            case "unmark":
                return unmarkTask(args, tasks, storage);

            case "todo":
                return addTodo(args, tasks, storage);

            case "deadline":
                return addDeadline(args, tasks, storage);

            case "event":
                return addEvent(args, tasks, storage);

            case "find":
                return findTasks(args, tasks);

            case "snooze":
                return snoozeTask(args, tasks, storage);

            default:
                throw new PeterException("Sorry, I do not know what that means. Would you like to add " +
                        "a task using 'todo', 'deadline' or 'event'?");
            }

        } catch (PeterException | DateTimeParseException | NumberFormatException | IndexOutOfBoundsException e) {
            return e.getMessage();
        }
    }

    public static int checkIndex(String indexStr, int size) throws PeterException {
        try {
            int index = Integer.parseInt(indexStr) - 1;
            if (index < 0 || index >= size) {
                throw new PeterException("Invalid index. Item does not exist in list.");
            }
            return index;

        } catch (NumberFormatException e) {
            throw new PeterException("Invalid index format. Please enter a number.");
        }
    }

    public static String listTasks(TaskList tasks) {
        if (tasks.size() == 0) {
            return "Nothing in list yet.";
        }
        return tasks.getAllTasksAsString();
    }

    public static String markTask(String indexStr, TaskList tasks, Storage storage) throws PeterException {
        int index = checkIndex(indexStr, tasks.size());
        tasks.get(index).markAsDone();
        storage.saveFile(tasks.getAllTasks());
        return formatString("Keep it up! I've marked this task as done:", tasks.get(index).toString());
    }

    public static String unmarkTask(String indexStr, TaskList tasks, Storage storage) throws PeterException {
        int index = checkIndex(indexStr, tasks.size());
        tasks.get(index).markAsUndone();
        storage.saveFile(tasks.getAllTasks());
        return formatString("Keep it up! I've unmarked this task:", tasks.get(index).toString());
    }

    public static String deleteTask(String indexStr, TaskList tasks, Storage storage) throws PeterException {
        int index = checkIndex(indexStr, tasks.size());
        Task deletedTask = tasks.delete(index);
        storage.saveFile(tasks.getAllTasks());
        return formatString("Noted. I've removed this task:", deletedTask.toString(),
                "Now you have " + tasks.size() + " tasks in your list.");
    }

    public static String addTodo(String args, TaskList tasks, Storage storage) throws PeterException {
        if (args.isEmpty()) {
            throw new PeterException("Sorry! Description of todo cannot be empty.");
        }
        Task todo = new Todo(args);
        tasks.add(todo);
        storage.saveFile(tasks.getAllTasks());
        return formatString(">> Got it! I've added this task:", todo.toString(),
                "Now you have " + tasks.size() + " tasks in your list.");
    }

    public static String addDeadline(String args, TaskList tasks, Storage storage) throws PeterException {
        if (args.isEmpty()) {
            throw new PeterException("Sorry! Description of deadline cannot be empty.");
        }
        if (!args.contains(" /by ")) {
            throw new PeterException("Incorrect format. Should be deadline <task> /by <time>");
        }

        String[] dSplit = args.split(" /by ", 2);
        try {
            LocalDate date = LocalDate.parse(dSplit[1]);
            Task deadline = new Deadline(dSplit[0], date);
            tasks.add(deadline);
            storage.saveFile(tasks.getAllTasks());
            return formatString(">> Got it! I've added this task:",
                    deadline.toString(),
                    "Now you have " + tasks.size() + " tasks in your list.");
        } catch (DateTimeParseException e) {
            throw new PeterException("Invalid Date Format! Should be yyyy-mm-dd (e.g. 2024-01-30).");
        }
    }

    public static String addEvent(String args, TaskList tasks, Storage storage) throws PeterException {
        if (args.isEmpty()) {
            throw new PeterException("Sorry! Description of deadline cannot be empty.");
        }

        if (!args.contains(" /from ") || !args.contains(" /to ")) {
            throw new PeterException("Incorrect format. Should be event <task> /from <time> /to <time>");
        }

        // read book /from 12 June /to 14 June
        String[] inputArr = args.split(" /from ");
        String description = inputArr[0];
        String[] dateArr = inputArr[1].split(" /to ");
        String start = dateArr[0];
        String end = dateArr[1];
        Task event = new Event(description, start, end);
        tasks.add(event);
        storage.saveFile(tasks.getAllTasks());
        return formatString(">> Got it! I've added this task:",
                event.toString(),
                "Now you have " + tasks.size() + " tasks in your list.");
    }

    public static String findTasks(String args, TaskList tasks) throws PeterException {
        if (args.isEmpty()) {
            throw new PeterException("Sorry! Description to find cannot be empty.");
        }
        List<Task> foundTasks = tasks.findTasks(args);
        if (foundTasks.isEmpty()) {
            throw new PeterException("There are no matching tasks in your list.");
        }
        String taskStr = "Here are the matching tasks in your list:\n";
        for (int i = 0; i < foundTasks.size(); i++) {
            taskStr += i + 1 + "." + foundTasks.get(i);
            if (i != foundTasks.size() - 1) {
                taskStr += "\n";
            }
        }
        return taskStr;
    }

    public static String snoozeTask(String args, TaskList tasks, Storage storage) throws PeterException {
        String[] inputArr = args.split(" ", 2);
        if (inputArr.length < 2) {
            throw new PeterException("Sorry! Please give me the task index you would like to snooze, and the new date(s)!");
        }

        // snooze "2 /from 240226 /to 260226" split to ["2", "/from 240226 /to 260226"]
        int index = checkIndex(inputArr[0], tasks.size());
        Task task = tasks.get(index);
        String dateStr = " " + inputArr[1];

        if (task instanceof Todo) {
            throw new PeterException("Sorry! Todo task can't be snoozed as it does not have a date!");
        }

        if (task instanceof Deadline) {
            if (!dateStr.contains(" /by ")) {
                throw new PeterException("Incorrect format! For deadlines, format: snooze <index> /by <yyyy-mm-dd>.");
            }
            try {
                // dateStr = " /by 250226" split to ["", "250226"]
                String[] dateArr = dateStr.split(" /by ", 2);
                LocalDate newDate = LocalDate.parse(dateArr[1]);
                ((Deadline) task).setBy(newDate);
            } catch (DateTimeParseException e) {
                throw new PeterException("Invalid Date Format! Should be yyyy-mm-dd (e.g. 2024-01-30).");
            }
        }

        if (task instanceof Event) {
            if (!dateStr.contains(" /from ") || !dateStr.contains(" /to ")) {
                throw new PeterException("Incorrect format! For events, use format: snooze <index> /from <date> /to <date>");
            }
            // dateStr = " /from 250226 /to 260226" split to ["", "250226 /to 260226"]
            String[] dateArr = dateStr.split(" /from ", 2)[1].split(" /to ", 2);
            ((Event) task).setStartEnd(dateArr[0], dateArr[1]);
        }

        storage.saveFile(tasks.getAllTasks());
        return formatString("Got it! I've snoozed this task to the new date: ", task.toString());
    }

    /**
     * Returns a formatted string joined by newlines.
     * @param input Variable number of strings to join.
     */
    public static String formatString(String ... input) {
        StringBuilder sb = new StringBuilder();
        for (String i : input) {
            sb.append(i).append("\n");
        }
        return sb.toString();
    }
}