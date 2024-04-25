package ru.yandex.javacource.alexandrov.schedule.manager;

import org.junit.jupiter.api.Test;
import ru.yandex.javacource.alexandrov.schedule.exceptions.ManagerSaveException;
import ru.yandex.javacource.alexandrov.schedule.tasks.Epic;
import ru.yandex.javacource.alexandrov.schedule.tasks.Subtask;
import ru.yandex.javacource.alexandrov.schedule.tasks.Task;
import ru.yandex.javacource.alexandrov.schedule.tasks.TaskStatus;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {
    TaskManager taskManager = Managers.getDefault();


    @Test
    void addInMemoryTaskManagerDifferentTypes() throws ManagerSaveException {
        Task task = new Task("Задача 1", "Test addNewTask description", TaskStatus.NEW);
        Epic epic = new Epic("Купить продукты", "Еда и напитки", TaskStatus.NEW);

        final int taskId = taskManager.addNewTask(task);
        final Task savedTask = taskManager.getTaskById(taskId);
        final int epicId = taskManager.addNewEpic(epic);
        final Epic savedEpic = taskManager.getEpicById(epicId);

        Subtask subtask = new Subtask("Еда", "Макароны",  TaskStatus.NEW, epicId);
        final int subtaskId = taskManager.addNewSubtask(subtask);
        final List<Subtask> savedSubtask = taskManager.getSubtasksByEpicId(epicId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertNotNull(savedEpic , "Задача не найдена.");
        assertNotNull(savedSubtask, "Задача не найдена.");

        final List<Task> tasks = taskManager.getAllTasks();
        final List<Epic> epics = taskManager.getAllEpics();
        final List<Subtask> subtasks = taskManager.getAllSubtasks();

        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
        assertEquals(epic, epics.get(0), "Задачи не совпадают.");
        assertEquals(subtask, subtasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void NotConflictTasks() throws ManagerSaveException {
        Task task = new Task("Test addNewTask", "Test addNewTask description", 3, TaskStatus.NEW);
        Task taskGenerate = new Task("Test addNewTask", "Test addNewTask description", TaskStatus.NEW);

        final int taskGenId = taskManager.addNewTask(taskGenerate);

        assertNotEquals(task.getId(), taskGenId, "Идентификаторы задач конфликтуют");
    }

    @Test
    void taskFieldsNotChange() throws ManagerSaveException {
        Task task = new Task("Test addNewTask", "Test addNewTask description", TaskStatus.NEW);

        final int taskGenId = taskManager.addNewTask(task);
        final Task savedTask = taskManager.getTaskById(taskGenId);

        assertEquals(savedTask.getName(), task.getName(), "Имена Задач не совпадают.");
        assertEquals(savedTask.getDescription(), task.getDescription(), "Описания Задач не совпадают.");
        assertEquals(savedTask.getId(),task.getId(), "Идентификаторы задач не совпадают.");
        assertEquals(savedTask.getStatus(),task.getStatus(), "Статусы задач не совпадают.");

        assertTrue(savedTask == task, "Задачи не совпадают.");
    }

    @Test
    void addNewTask() throws ManagerSaveException {
        Task task = new Task("Test addNewTask", "Test addNewTask description", TaskStatus.NEW);

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
    void hereditaryTaskEqualsIfTaskIdEquals() throws ManagerSaveException {
        Task epic = new Epic("Купить продукты", "Еда и Напитки", TaskStatus.NEW);
        Subtask subtask = new Subtask("Еда", "Макароны", TaskStatus.NEW,
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

        assertEquals(1, epic.addSubtaskIdTest(epic.getId()), "Эпик добавлен как своя подзадача");
    }

    @Test
    void testSetSubtaskAsItsOwnEpic() {
        Epic epic = new Epic("Уборка", "По ситуации", 0, TaskStatus.NEW);
        Subtask subtask = new Subtask("Еда", "Макароны", 1, TaskStatus.NEW, epic.getId());

        final int id = subtask.setEpicIdTest(epic.getId());

        assertEquals(0, subtask.setEpicIdTest(epic.getId()), "Нельзя установить подзадачу своим же эпиком");
    }

    @Test
    void testEpicHasOnlyActualSubtasks() {
        Epic epic = new Epic("Купить продукты", "Еда и Напитки", TaskStatus.NEW);
        Subtask subtask = new Subtask("Еда", "Макароны", TaskStatus.NEW,
                epic.getId());
        Subtask subtask2 = new Subtask("Напитки", "Сок",  TaskStatus.NEW,
                epic.getId());

        epic.addSubtaskId(subtask.getId());
        epic.addSubtaskId(subtask2.getId());

        ArrayList<Integer> subT = new ArrayList<>();
        subT.add(subtask2.getId());

        epic.setSubtaskIds(subT);


        assertEquals(1, epic.getSubtaskIds().size());
        assertTrue(epic.getSubtaskIds().contains(subtask2.getId()));

    }

    @Test
    public void testTaskFieldUpdate() throws ManagerSaveException {
        Task task = new Task("Test addNewTask", "Test addNewTask description", TaskStatus.NEW);
        taskManager.addNewTask(task);

        task.setDescription("Обновили описание");

        Task updatedTask = taskManager.getTaskById(task.getId());
        assertEquals("Обновили описание", updatedTask.getDescription());
    }



}

