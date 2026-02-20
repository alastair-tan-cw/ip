package peter.parser;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import peter.exception.PeterException;
import peter.task.Deadline;
import peter.task.TaskList;
import peter.storage.Storage;
import peter.task.Todo;

import java.time.LocalDate;

public class ParserTest {

    @Test
    public void checkIndex_validIndex_returnsZeroBasedIndex() throws PeterException {
        // If a user types "2" and the list has 5 items, it should return array index 1
        assertEquals(1, Parser.checkIndex("2", 5));
    }

    @Test
    public void checkIndex_indexOutOfBounds_throwsPeterException() {
        assertThrows(PeterException.class, () -> {
            Parser.checkIndex("6", 5);
        });
    }

    @Test
    public void checkIndex_notANumber_throwsPeterException() {
        assertThrows(PeterException.class, () -> {
            Parser.checkIndex("abc", 5);
        });
    }

    @Test
    public void addDeadline_missingBy_throwsPeterException() {
        TaskList list = new TaskList();
        Storage storage = new Storage("./data/test.txt");

        PeterException exception = assertThrows(PeterException.class, () -> {
            Parser.addDeadline("return book", list, storage);
        });
        assertEquals("Incorrect format. Should be deadline <task> /by <time>", exception.getMessage());
    }

    @Test
    public void addEvent_endBeforeStart_throwsPeterException() {
        TaskList list = new TaskList();
        Storage storage = new Storage("./data/test.txt");
        String input = "team meeting /from 2026-10-20 /to 2026-10-10";

        PeterException exception = assertThrows(PeterException.class, () -> {
            Parser.addEvent(input, list, storage);
        });
        assertEquals("Wait! An event can't end before or on the same date it starts!", exception.getMessage());
    }

    @Test
    public void addEvent_invalidDate_throwsPeterException() {
        TaskList list = new TaskList();
        Storage storage = new Storage("./data/test.txt");
        String input = "event team meeting /from 2026-02-30 /to 2026-03-01";

        assertThrows(PeterException.class, () -> {
            Parser.addEvent(input, list, storage);
        });
    }

    @Test
    public void findTasks_noMatchFound_throwsPeterException() {
        TaskList list = new TaskList();
        list.add(new Todo("read book"));

        PeterException exception = assertThrows(PeterException.class, () -> {
            Parser.findTasks("eat", list);
        });
        assertEquals("There are no matching tasks in your list.", exception.getMessage());
    }

    @Test
    public void snoozeTask_deadlineDateWrongFormat_throwsPeterException() {
        TaskList list = new TaskList();
        Storage storage = new Storage("./data/test.txt");
        list.add(new Deadline("return book", LocalDate.parse("2026-02-25"))); // Index 1

        PeterException exception = assertThrows(PeterException.class, () -> {
            Parser.snoozeTask("1 by 2026-02-27", list, storage);
        });
        assertEquals("Incorrect format! For deadlines, format: snooze <index> /by <yyyy-mm-dd>.", exception.getMessage());
    }
}
