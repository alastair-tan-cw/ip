import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Peter {
    public static final String name = "Peter";

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
            Task deadline = new Deadline(inputArr[0], inputArr[1]);
            list.add(deadline);
            printOutput(">> Got it! I've added this task:\n" +
                    deadline +
                    "\nNow you have " + list.size() + " tasks in your list.");
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
                printOutput("Keep it up! I've marked this task as done:\n" +
                        task);
            } else {
                task.markAsUndone();
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
        printOutput("Noted. I've removed this task:\n" +
                task +
                "\nNow you have " + list.size() + " tasks in your list.");
    }

    public static void main(String[] args) {
        List<Task> list = new ArrayList<>();
        String menu = " Hello! I'm " + name + "\n" +
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
