package ru.yandex.javacource.alexandrov.schedule.manager;

import ru.yandex.javacource.alexandrov.schedule.exceptions.ManagerSaveException;
import ru.yandex.javacource.alexandrov.schedule.tasks.Epic;
import ru.yandex.javacource.alexandrov.schedule.tasks.Subtask;
import ru.yandex.javacource.alexandrov.schedule.tasks.Task;

import java.util.List;

public interface TaskManager {
    List<Task> getAllTasks();

    List<Epic> getAllEpics();

    List<Subtask> getAllSubtasks();

    void deleteAllTasks() throws ManagerSaveException;

    void deleteAllEpics() throws ManagerSaveException;

    void deleteAllSubtasks() throws ManagerSaveException;

    Task getTaskById(int taskId);

    Epic getEpicById(int taskId);

    Subtask getSubtaskById(int id);

    List<Subtask> getSubtasksByEpicId(int epicId);

    int addNewTask(Task task) throws ManagerSaveException;

    int addNewEpic(Epic epic) throws ManagerSaveException;

    Integer addNewSubtask(Subtask subtask) throws ManagerSaveException;

    void updateTasks(Task task) throws ManagerSaveException;

    void updateEpics(Epic epic) throws ManagerSaveException;

    void updateSubtasks(Subtask subtask) throws ManagerSaveException;

    void deleteTaskById(int taskId) throws ManagerSaveException;

    void deleteEpicsById(int taskId) throws ManagerSaveException;

    void deleteSubtask(int id) throws ManagerSaveException;

    List<Task> getHistoryManager();
}
