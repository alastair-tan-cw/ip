import java.util.Scanner;

public class Peter {
    public static final String name = "Peter";

    public static void main(String[] args) {
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
            } else {
                System.out.println("____________________________________________________________\n" +
                        ">> " + userInput +
                        "\n____________________________________________________________\n");
            }
        }
    }
}
