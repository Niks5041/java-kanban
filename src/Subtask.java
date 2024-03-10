import java.util.ArrayList;
public class Subtask extends Epic {
    public Subtask(String name, String description, int taskId, TaskStatus status, ArrayList<Subtask> subtask) {
        super(name, description, taskId, status, subtask);
    }
}

