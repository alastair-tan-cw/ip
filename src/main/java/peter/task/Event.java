package peter.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task that starts at a specific time and ends at a specific time.
 */
public class Event extends Task {
    protected LocalDate start;
    protected LocalDate end;

    /**
     * Creates a new Event task.
     *
     * @param description The description of the event.
     * @param start       The start date of the event.
     * @param end         The end date of the event.
     */
    public Event(String description, LocalDate start, LocalDate end) {
        super(description);
        this.start = start;
        this.end = end;
    }

    /**
     * Returns the start time of the event.
     *
     * @return The start date as a LocalDate object.
     */
    public LocalDate getStart() {
        return this.start;
    }

    /**
     * Returns the end time of the event.
     *
     * @return The end date as a LocalDate object.
     */
    public LocalDate getEnd() {
        return this.end;
    }

    /**
     * Updates the start and end dates of the event.
     *
     * @param start The new start date.
     * @param end   The new end date.
     */
    public void setStartEnd(LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Returns a string representation of the event task.
     * The date is formatted as "MMM dd yyyy" (e.g. Oct 25 2026).
     *
     * @return The formatted string of the event.
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " +
                start.format(DateTimeFormatter.ofPattern("MMM dd yyyy")) +
                " to: " +
                end.format(DateTimeFormatter.ofPattern("MMM dd yyyy")) + ")";
    }
}
