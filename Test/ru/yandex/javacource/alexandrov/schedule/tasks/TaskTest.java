package ru.yandex.javacource.alexandrov.schedule.tasks;

import org.junit.jupiter.api.Test;
import ru.yandex.javacource.alexandrov.schedule.manager.HistoryManager;
import ru.yandex.javacource.alexandrov.schedule.manager.Managers;
import ru.yandex.javacource.alexandrov.schedule.manager.TaskManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    TaskManager taskManager = Managers.getDefault();
    HistoryManager historyManager = Managers.getDefaultHistory();


    @Test
    void addNewTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description",
                taskManager.getGeneratorId(), TaskStatus.NEW);
        final int taskId = taskManager.addNewTask(task);

        final Task savedTask = taskManager.getTaskById(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getAllTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void add() {
        Task task1 = new Task("Сделать уроки", "Java", taskManager.getGeneratorId(), TaskStatus.NEW);
        historyManager.add(task1);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    void hereditaryTaskEqualsIfTaskIdEquals() {
        Task epic = new Epic("Купить продукты", "Еда и Напитки", taskManager.getGeneratorId(),
                TaskStatus.NEW);
        Subtask subtask = new Subtask("Еда", "Макароны", taskManager.getGeneratorId(), TaskStatus.NEW,
                epic.getId());

        final int taskId = taskManager.addNewEpic((Epic) epic);
        final Task savedEpic = taskManager.getEpicById(taskId);

        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");
    }

    @Test
    void testAddEpicToItselfAsSubtask() {
        Epic epic = new Epic("Уборка", "По ситуации", 1,
                TaskStatus.NEW);

        assertEquals(-1, epic.addSubtaskIdTest(epic.getId()), "Эпик добавлен как своя подзадача");
    }

    @Test
    void testSetSubtaskAsItsOwnEpic() {
        Epic epic = new Epic("Уборка", "По ситуации", 0, TaskStatus.NEW);
        Subtask subtask = new Subtask("Еда", "Макароны", 1, TaskStatus.NEW, epic.getId());

        final int id = subtask.setEpicIdTest(epic.getId());

        assertEquals(0, subtask.setEpicIdTest(epic.getId()), "Нельзя установить подзадачу своим же эпиком");
    }
}

