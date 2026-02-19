package peter.task;

import java.util.List;
import java.util.ArrayList;

/**
 * Supports operations to get, add, delete tasks from list.
 */
public class TaskList {
    private List<Task> tasks;

    /**
     * Creates a TaskList with an existing list of tasks.
     *
     * @param tasks The initial list of tasks.
     */
    public TaskList(List<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Creates an empty TaskList.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Adds a new task to the list.
     *
     * @param task The task object to be added.
     */
    public void add(Task task) {
        assert task != null : "I cannot add a task that is null!";
        tasks.add(task);
    }

    /**
     * Removes a task from the list at the given index.
     *
     * @param index The index of the task to delete.
     * @return The task that was removed.
     */
    public Task delete(int index) {
        assert index >= 0 && index < tasks.size() : "Index is out of bounds";
        return tasks.remove(index);
    }

    /**
     * Retrieves the task at the given index.
     *
     * @param index The index of the task to retrieve.
     * @return The task at the given index.
     */
    public Task get(int index) {
        assert index >= 0 && index < tasks.size() : "Index is out of bounds!";
        return tasks.get(index);
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return The total number of tasks.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the full list of tasks.
     *
     * @return The list of tasks.
     */
    public List<Task> getAllTasks() {
        return tasks;
    }

    /**
     * Returns a formatted string of all tasks in the list.
     *
     * @return The list of tasks in String format.
     */
    public String getAllTasksAsString() {
        String listStr = "Here are the tasks in your list:\n";
        for (int i = 0; i < tasks.size(); i++) {
            listStr += i + 1 + "." + tasks.get(i);
            if (i != tasks.size() - 1) {
                listStr += "\n";
            }
        }
        return listStr;
    }

    /**
     * Returns a list of tasks containing provided keyword.
     *
     * @return The list of tasks in String format.
     */
    public List<Task> findTasks(String input) {
        List<Task> foundTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.toString().contains(input)) {
                foundTasks.add(task);
            }
        }
        return foundTasks;
    }
}