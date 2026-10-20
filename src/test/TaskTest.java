import org.example.minimini.Task;
import org.junit.jupiter.api.Test;

import static org.springframework.test.util.AssertionErrors.*;

public class TaskTest {
@Test
 void checknuevaTarea() {
Task task = new Task(0, "hola");
assertFalse("test", task.isCompleted());
}
}
