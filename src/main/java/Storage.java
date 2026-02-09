import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Storage {
    public static final String FILE_PATH = "./data/peter.txt";
    private String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    public List<Task> loadFile() throws PeterException{
        List<Task> list = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            return list;
        }

        try {
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
        return list;
    }

    public void saveFile(List<Task> list){
        try {
            // Handle case where folder doesn't exist yet
            File file = new File(filePath);
            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }

            FileWriter writer = new FileWriter(filePath);

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
}
