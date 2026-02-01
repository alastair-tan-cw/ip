import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Peter {
    public static final String name = "Peter";

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        String menu = "____________________________________________________________\n" +
                " Hello! I'm " + name + "\n" +
                " What can I do for you?\n" +
                "____________________________________________________________\n";
        System.out.println(menu);

        Scanner sc = new Scanner(System.in);
        while (true) {
            String userInput = sc.nextLine();
            if (userInput.equals("bye")) {
                System.out.println("____________________________________________________________\n" +
                        "Goodbye. Chat again soon?" +
                        "\n____________________________________________________________\n");
                break;
            } else if (userInput.equals("list")) {
                if (list.isEmpty()) {
                    System.out.println("____________________________________________________________\n" +
                            "Nothing in list yet." +
                            "\n____________________________________________________________\n");
                    continue;
                }

                System.out.println("____________________________________________________________");
                for (int i = 0; i < list.size(); i++) {
                    System.out.println(i + 1 + "." + list.get(i));
                }
                System.out.println("____________________________________________________________\n");
            } else {
                list.add(userInput);
                System.out.println("____________________________________________________________\n" +
                        ">> Added: " + userInput +
                        "\n____________________________________________________________\n");
            }
        }
    }
}
