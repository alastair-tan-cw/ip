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
    public static final String FILE_PATH = "./data/peter.txt";

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
            saveFile(list);
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
                saveFile(list);
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
            saveFile(list);
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
                saveFile(list);
                printOutput("Keep it up! I've marked this task as done:\n" +
                        task);
            } else {
                task.markAsUndone();
                saveFile(list);
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
        saveFile(list);
        printOutput("Noted. I've removed this task:\n" +
                task +
                "\nNow you have " + list.size() + " tasks in your list.");
    }

    public static void loadFile(List<Task> list){
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                return;
            }
            Scanner sc = new Scanner(file);

            while (sc.hasNext()) {
                String[] parts = sc.nextLine().split(" \\| "); // Split by " | "

                // task details in the .txt file
                String type = parts[0];
                boolean isDone = parts[1].equals("1");
                String description = parts[2];

                Task task = null;

                switch (type) {
                    case "T":
                        task = new Todo(description);
                        break;
                    case "D":
                        LocalDate by = LocalDate.parse(parts[3]);
                        task = new Deadline(description, by);
                        break;
                    case "E":
                        String start = parts[3];
                        String end = parts[4];
                        task = new Event(description, start, end);
                        break;
                }

                // if not corrupted/improper format then add to list
                if (task != null) {
                    if (isDone) {
                        task.markAsDone();
                    }
                    list.add(task);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No saved tasks found. Starting with empty list.");
        } catch (Exception e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }

    public static void saveFile(List<Task> list){
        try {
            // Handle case where folder doesn't exist yet
            File file = new File(FILE_PATH);
            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }

            FileWriter writer = new FileWriter(FILE_PATH);

            for (Task task : list) {
                String type = "";
                String dates = "";

                if (task instanceof Todo) {
                    type = "T";
                } else if (task instanceof Deadline) {
                    type = "D";
                    Deadline deadline = (Deadline) task;
                    dates = " | " + deadline.by;
                } else if (task instanceof Event) {
                    type = "E";
                    Event event = (Event) task;
                    dates = " | " + event.start + " | " + event.end;
                }

                String isDone = task.isDone ? "1" : "0"; // Assuming 'isDone' is boolean
                String eachTask = type + " | " + isDone + " | " + task.description + dates;

                writer.write(eachTask + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        List<Task> list = new ArrayList<>();
        loadFile(list);
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
