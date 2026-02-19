package peter.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task that needs to be done before a specific date.
 */
public class Deadline extends Task {
    protected LocalDate by;

    /**
     * Creates a new Deadline task.
     *
     * @param description The description of the task.
     * @param by          The date by which the task must be completed.
     */
    public Deadline(String description, LocalDate by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns the deadline date.
     *
     * @return The due date of the task.
     */
    public LocalDate getBy() {
        return this.by;
    }

    public void setBy(LocalDate by) {
        this.by = by;
    }

    /**
     * Returns a string representation of the deadline task.
     * The date is formatted as "MMM dd yyyy" (e.g. Oct 25 2026).
     *
     * @return The formatted string of the deadline.
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by.format(DateTimeFormatter.ofPattern("MMM dd yyyy"))+ ")";
    }
}