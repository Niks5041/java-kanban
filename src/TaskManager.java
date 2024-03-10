import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int taskIdCounter = 1;
    private int epicIdCounter = 1;
    private int subtaskIdCounter = 1;
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epicTasks = new HashMap<>();
    HashMap<Integer, ArrayList<Subtask>> epicSubtasks = new HashMap<>();

    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> task1 = new ArrayList<>();
        for (Task task : tasks.values()) {
            task1.add(task);
        }
        return task1;
    }

    public ArrayList<Epic> getAllEpicTasks() {
        return new ArrayList<>(epicTasks.values());
    }

    public ArrayList<Subtask> getAllEpicSubtasks() {
        ArrayList<Subtask> allSubtasks = new ArrayList<>();
        for (ArrayList<Subtask> subtasks : epicSubtasks.values()) {
            allSubtasks.addAll(subtasks);
        }
        return allSubtasks;
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllEpicTasks() {
        epicTasks.clear();
    }

    public void deleteAllEpicSubtasks() {
        epicSubtasks.clear();
    }

    public Task getTasksById(int taskId) {
        return tasks.get(taskId);
    }

    public Epic getEpicTasksById(int taskId) {
        return epicTasks.get(taskId);
    }

    public Subtask getEpicSubTasksById(int taskId) {
        for (ArrayList<Subtask> subtasks : epicSubtasks.values()) {
            for (Subtask subtask : subtasks) {
                if(subtask.getId() == taskId) {
                    return subtask;
                }
            }
        }
        return null;
    }

    public void createNewTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void createNewEpicTask(Epic epicTask) {
        epicTasks.put(epicTask.getId(), epicTask);
        epicSubtasks.put(epicTask.getId(), new ArrayList<>());
    }

    public void createNewEpicSubTask(Subtask epicSubtask) {
        ArrayList<Subtask> subtasks = new ArrayList<>();
        subtasks.add(epicSubtask);
        epicSubtasks.put(epicSubtask.getId(), subtasks);
    }

    public void updateTask(Task updatedTask) {
        tasks.put(updatedTask.getId(), updatedTask);
    }

    public void updateEpicTask(Epic updaterEpicTask) {
        epicTasks.put(updaterEpicTask.getId(), updaterEpicTask);
    }

    public void updateEpicSubTask(Subtask updatedEpicSubtask) {
        ArrayList<Subtask>updatedSubtask = new ArrayList<>();
        epicSubtasks.put(updatedEpicSubtask.getId(), updatedSubtask );
    }

    public void deleteTaskById(int taskId) {
        tasks.remove(taskId);
    }

    public void deleteEpicTaskById(int taskId) {
        ArrayList<Subtask> subtasks = getArrayOfEpicSubtask(taskId);
        for (Subtask subtask : subtasks) {
            epicSubtasks.remove(subtask);
        }
        epicTasks.remove(taskId);
    }

    public void deleteEpicSubtaskById(int taskId) {
        epicSubtasks.remove(taskId);
        epicTasks.remove(taskId);
    }

    public ArrayList<Subtask> getArrayOfEpicSubtask(int taskId) {
        Epic epic = epicTasks.get(taskId);
        ArrayList<Subtask> epic1 = epic.getSubtask();
        return epic1;
    }

    public void updateTaskStatuses(int taskId) {
        Task task = tasks.get(taskId);
        if (task.getStatus().equals(TaskStatus.NEW)) {
            return;
        } else if (task.getStatus().equals(TaskStatus.IN_PROGRESS)) {
            task.setStatus(TaskStatus.IN_PROGRESS);
        } else if (task.getStatus().equals(TaskStatus.DONE)) {
            task.setStatus(TaskStatus.DONE);
            deleteTaskById(taskId);
        }
    }

    public void updateEpicStatuses(int taskId) {
        Epic epic = epicTasks.get(taskId);
        ArrayList<Subtask> subtasks = getArrayOfEpicSubtask(taskId);
        for (Subtask subtask : subtasks) {
            if (epic.getSubtask() == null || subtask.getStatus().equals(TaskStatus.NEW)) {
                epic.setStatus(TaskStatus.NEW);
            } else {
                epic.setStatus(TaskStatus.IN_PROGRESS);
            }
        }
        for (Subtask subtask1 : subtasks) {
            if (subtask1.getStatus().equals(TaskStatus.DONE)) {
                    epic.setStatus(TaskStatus.DONE);
            } else {
                epic.setStatus(TaskStatus.IN_PROGRESS);
            }
        }
    }

    public int generateTaskId() {
        return taskIdCounter++;
    }

    public int generateEpicId() {
        return epicIdCounter++;
    }

    public int generateSubtaskId() {
        return subtaskIdCounter++;
    }

}
