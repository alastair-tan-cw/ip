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
        List<String> list = new ArrayList<>();
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

                String listStr = "";
                for (int i = 0; i < list.size(); i++) {
                    listStr += i + 1 + "." + list.get(i);
                    if (i != list.size() - 1) {
                        listStr += "\n";
                    }
                }
                printOutput(listStr);
            } else {
                list.add(userInput);
                printOutput(">> Added: " + userInput);
            }
        }
    }
}
