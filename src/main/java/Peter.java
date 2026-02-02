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
            } else if (userInput.equals("list")) {
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
            } else if (userInput.startsWith("mark") || userInput.startsWith("unmark")) {
                // then mark the right item
                String[] splitStr = userInput.split(" ");
                String invalidMsg = "Invalid index. Item does not exist in list.";

                try {
                    int markIndex = Integer.parseInt(splitStr[1]) - 1;

                    if (markIndex < 0 || markIndex >= list.size()) {
                        printOutput(invalidMsg);
                        continue;
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
                } catch (Exception e) {
                    printOutput(invalidMsg);
                }
            } else if (userInput.startsWith("todo")) {
                if (userInput.length() <= 5) {
                    printOutput("Sorry! Description of todo cannot be empty.");
                    continue;
                }
                userInput = userInput.substring(5);
                Task todo = new Todo(userInput);
                list.add(todo);
                printOutput(">> Got it! I've added this task:\n" +
                        todo +
                        "\nNow you have " + list.size() + " tasks in your list.");

            } else if (userInput.startsWith("deadline")) {
                if (userInput.length() <= 9) {
                    printOutput("Sorry! Description of deadline cannot be empty.");
                    continue;
                }
                userInput = userInput.substring(9);

                if (!userInput.contains(" /by ")) {
                    printOutput("Incorrect format. Should be deadline <task> /by <time>");
                    continue;
                }

                String[] inputArr = userInput.split(" /by ");
                Task deadline = new Deadline(inputArr[0], inputArr[1]);
                list.add(deadline);
                printOutput(">> Got it! I've added this task:\n" +
                        deadline +
                        "\nNow you have " + list.size() + " tasks in your list.");

            } else if (userInput.startsWith("event")) {
                if (userInput.length() <= 6) {
                    printOutput("Sorry! Description of event cannot be empty.");
                    continue;
                }

                userInput = userInput.substring(6);

                if (!userInput.contains(" /from ") || !userInput.contains(" /to ")) {
                    printOutput("Incorrect format. Should be event <task> /from <time> /to <time>");
                    continue;
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

            } else {
                printOutput("Sorry, I do not know what that means. Would you like to add\n" +
                        "a task using 'todo', 'deadline' or 'event'?");
            }
        }
    }
}
