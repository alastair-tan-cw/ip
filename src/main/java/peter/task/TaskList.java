package peter.task;

import java.util.List;
import java.util.ArrayList;

public class TaskList {
    private List<Task> tasks;

    public TaskList(List<Task> tasks) {
        this.tasks = tasks;
    }

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public void add(Task task) {
        tasks.add(task);
    }

    public Task delete(int index) {
        return tasks.remove(index);
    }

    public Task get(int index) {
        return tasks.get(index);
    }

    public int size() {
        return tasks.size();
    }

    public List<Task> getAllTasks() {
        return tasks;
    }

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