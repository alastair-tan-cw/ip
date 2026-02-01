import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Peter {
    public static final String name = "Peter";

    public static void printOutput (String input) {
        System.out.println("____________________________________________________________\n" +
                input +
                "\n____________________________________________________________\n");
    }

    public static void main(String[] args) {
        List<Task> list = new ArrayList<>();
        String menu = " Hello! I'm " + name + "\n" +
                " What can I do for you?\n";
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

                String listStr = "Here are the tasks in your list: \n";
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
                        printOutput("Keep it up! I've marked this task as done: \n" +
                                task);
                    } else {
                        task.markAsUndone();
                        printOutput("Got it. I've marked this task as not done yet: \n" +
                                task);
                    }
                } catch (Exception e) {
                    printOutput(invalidMsg);
                }

            } else {
                list.add(new Task(userInput));
                printOutput(">> Added: " + userInput);
            }
        }
    }
}
