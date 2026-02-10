package peter.task;

/**
 * Represents a task without any specific date or time constraints.
 */
public class Todo extends Task {
    /**
     * Creates a new Todo task.
     *
     * @param description The description of the task.
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns a string representation of the Todo task.
     *
     * @return The formatted string of the Todo.
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}