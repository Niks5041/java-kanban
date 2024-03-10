import java.util.ArrayList;
public class Epic extends Task {
    private ArrayList<Subtask> subtask;
    public Epic(String name, String description, int taskId, TaskStatus status, ArrayList<Subtask> subtask) {
        super(name, description, taskId, status);
        this.subtask = subtask;
    }

    public ArrayList<Subtask> getSubtask() {
        return subtask;
    }
}



