package ru.yandex.javacource.alexandrov.schedule.manager;


import org.junit.jupiter.api.Test;
import ru.yandex.javacource.alexandrov.schedule.exceptions.ManagerSaveException;
import ru.yandex.javacource.alexandrov.schedule.tasks.Epic;
import ru.yandex.javacource.alexandrov.schedule.tasks.Subtask;
import ru.yandex.javacource.alexandrov.schedule.tasks.Task;
import ru.yandex.javacource.alexandrov.schedule.tasks.TaskStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InMemoryHistoryManagerTest {

    TaskManager taskManager = Managers.getDefault();
    HistoryManager historyManager = Managers.getDefaultHistory();

    @Test
    void taskNotChange() throws ManagerSaveException {
        Task task = new Task("Задача 1", "Test addNewTask description", TaskStatus.NEW);
        Epic epic = new Epic("Эпик 1", "Еда и напитки", TaskStatus.NEW);

        final int taskId = taskManager.addNewTask(task);
        final int epicId = taskManager.addNewEpic(epic);

        Subtask subtask = new Subtask("Сабтаска 1", "Макароны", TaskStatus.NEW,
                epic.getId());

        final int subtaskId = taskManager.addNewTask(subtask);
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask);



        Task task2 = new Task("Новая Задача 1", "Test addNewTask description", taskId, TaskStatus.NEW);
        Epic epic2 = new Epic("Новый Эпик 2", "Новый эпик", epicId, TaskStatus.NEW);
        Subtask subtask2 = new Subtask("Новая Сабтаска 2", "Новая сабтаска", subtaskId, TaskStatus.NEW,
                epicId);
        taskManager.updateTasks(task2);
        taskManager.updateEpics(epic2);
        taskManager.updateSubtasks(subtask2);

        List<Task> taskHistory = historyManager.getHistory();

        assertEquals("Задача 1", taskHistory.get(0).getName(),
                "Название первой версии задачи должно быть 'Задача 1'");
        assertEquals("Эпик 1", taskHistory.get(1).getName(),
                "Название первой версии эпика должно быть 'Эпик 1'");
        assertEquals("Сабтаска 1", taskHistory.get(2).getName(),
                "Название первой версии задачи должно быть 'Сабтаска 1'");
    }

    @Test
    void checkAddAndRemoveHistory() throws ManagerSaveException {
        Task task = new Task("Задача 1", "Test addNewTask description", TaskStatus.NEW);
        Epic epic = new Epic("Эпик 1", "Еда и напитки", TaskStatus.NEW);

        final int taskId = taskManager.addNewTask(task);
        final int epicId = taskManager.addNewEpic(epic);

        Subtask subtask = new Subtask("Сабтаска 1", "Макароны", TaskStatus.NEW,
                epic.getId());

        final int subtaskId = taskManager.addNewTask(subtask);
        historyManager.add(task);
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(epic);
        historyManager.add(subtask);

        List<Task> taskHistory = historyManager.getHistory();

        assertNotNull(taskHistory, "Задача не найдена.");

        historyManager.remove(taskId);
        historyManager.remove(epicId);
        historyManager.remove(subtaskId);

        List<Task> taskHistoryEmpty = historyManager.getHistory();

        assertNotNull(taskHistoryEmpty, "Задача найдена.");
    }
}




