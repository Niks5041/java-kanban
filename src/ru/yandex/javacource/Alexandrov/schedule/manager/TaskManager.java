package ru.yandex.javacource.alexandrov.schedule.manager;

import ru.yandex.javacource.alexandrov.schedule.tasks.Epic;
import ru.yandex.javacource.alexandrov.schedule.tasks.Subtask;
import ru.yandex.javacource.alexandrov.schedule.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    ArrayList<Task> getAllTasks();

    ArrayList<Epic> getAllEpics();

    ArrayList<Subtask> getAllSubtasks();

    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubtasks();

    Task getTaskById(int taskId);

    Epic getEpicById(int taskId);

    ArrayList<Subtask> getSubtasksById(int epicId);

    int addNewTask(Task task);

    int addNewEpic(Epic epic);

    Integer addNewSubtask(Subtask subtask);

    void updateTasks(Task task);

    void updateEpics(Epic epic);

    void updateSubtasks(Subtask subtask);

    void deleteTaskById(int taskId);

    void deleteEpicsById(int taskId);

    void deleteSubtask(int id);

    void updateEpicStatuses(int taskId);

    int getGeneratorId();

    List<Task> getHistoryManager();

}
