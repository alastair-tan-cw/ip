package peter.task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskListTest {
    @Test
    public void add_validTask_success() {
        TaskList list = new TaskList();
        list.add(new Todo("read book"));
        assertEquals(1, list.size());
    }

    @Test
    public void delete_listSize_getIndex_success() {
        TaskList list = new TaskList();
        list.add(new Todo("read book"));
        list.add(new Todo("return book"));
        list.delete(0);
        assertEquals(1, list.size());
        assertEquals("[T][ ] return book", list.get(0).toString());
    }
}