package peter.task;

/**
 * Represents a task that starts at a specific time and ends at a specific time.
 */
public class Event extends Task {
    protected String start;
    protected String end;

    /**
     * Creates a new Event task.
     *
     * @param description The description of the event.
     * @param start       The start time of the event.
     * @param end         The end time of the event.
     */
    public Event(String description, String start, String end) {
        super(description);
        this.start = start;
        this.end = end;
    }

    /**
     * Returns the start time of the event.
     *
     * @return The start time string.
     */
    public String getStart() {
        return this.start;
    }

    /**
     * Returns the end time of the event.
     *
     * @return The end time string.
     */
    public String getEnd() {
        return this.end;
    }

    public void setStartEnd(String start, String end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Returns a string representation of the event task.
     *
     * @return The formatted string of the event.
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + start + " to: " + end + ")";
    }
}
