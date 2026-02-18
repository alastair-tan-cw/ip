package peter.storage;

import peter.task.Task;
import peter.task.Todo;
import peter.task.Deadline;
import peter.task.Event;
import peter.exception.PeterException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Handles the loading and saving of task data to a specified file on the hard drive.
 */
public class Storage {
    public static final String FILE_PATH = "./data/peter.txt";
    private String filePath;

    /**
     * Creates a new Storage object.
     *
     * @param filePath The relative path to the file where data is stored.
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads tasks from the data file.
     *
     * @return A list of tasks loaded from the file.
     * @throws PeterException If the file exists but cannot be read properly.
     */
    public List<Task> loadFile() throws PeterException {
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

    /**
     * Saves the current list of tasks to the data file.
     *
     * @param list The list of tasks to be saved.
     */
    public void saveFile(List<Task> list) {
        try {
            // Handle case where folder doesn't exist yet
            File file = new File(filePath);
            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }

            FileWriter writer = new FileWriter(filePath);

            for (Task task : list) {
                String eachTask = formatTask(task);
                writer.write(eachTask + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    /**
     * Converts a Task object into a formatted string for file storage.
     * Format: TYPE | STATUS | DESCRIPTION | TIME
     * E.g: D | 1 | return book | 2026-02-25
     *
     * @param task The task to convert.
     * @return A formatted string to be written in the text file when saved.
     */
    public String formatTask(Task task) {
        String type = "";
        String dates = "";

        if (task instanceof Todo) {
            type = "T";
        }

        if (task instanceof Deadline) {
            type = "D";
            Deadline deadline = (Deadline) task;
            dates = " | " + deadline.getBy();
        }

        if (task instanceof Event) {
            type = "E";
            Event event = (Event) task;
            dates = " | " + event.getStart() + " | " + event.getEnd();
        }

        String isDone = task.isDone() ? "1" : "0";
        return type + " | " + isDone + " | " + task.getDescription() + dates;
    }
}
