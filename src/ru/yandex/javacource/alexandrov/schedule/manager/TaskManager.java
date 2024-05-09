package ru.yandex.javacource.alexandrov.schedule.manager;

import ru.yandex.javacource.alexandrov.schedule.tasks.Epic;
import ru.yandex.javacource.alexandrov.schedule.tasks.Subtask;
import ru.yandex.javacource.alexandrov.schedule.tasks.Task;

import java.util.List;
import java.util.Set;

public interface TaskManager {

    List<Task> getAllTasks();

    List<Epic> getAllEpics();

    List<Subtask> getAllSubtasks();

    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubtasks();

    Task getTaskById(int taskId);

    Epic getEpicById(int taskId);

    Subtask getSubtaskById(int id);

    List<Subtask> getSubtasksByEpicId(int epicId);

    int addNewTask(Task task);

    int addNewEpic(Epic epic);

    Integer addNewSubtask(Subtask subtask);

    void updateTasks(Task task);

    void updateEpics(Epic epic);

    void updateSubtasks(Subtask subtask);

    void deleteTaskById(int taskId);

    void deleteEpicsById(int taskId);

    void deleteSubtask(int id);

    List<Task> getHistoryManager();

    Set<Task> getPrioritizedTasks();

    boolean checkValidation(Task task);
}
