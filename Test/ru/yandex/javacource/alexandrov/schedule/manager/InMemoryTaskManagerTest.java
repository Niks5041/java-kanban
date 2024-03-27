package ru.yandex.javacource.alexandrov.schedule.manager;

import org.junit.jupiter.api.Test;
import ru.yandex.javacource.alexandrov.schedule.tasks.Epic;
import ru.yandex.javacource.alexandrov.schedule.tasks.Subtask;
import ru.yandex.javacource.alexandrov.schedule.tasks.Task;
import ru.yandex.javacource.alexandrov.schedule.tasks.TaskStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InMemoryTaskManagerTest {
    TaskManager taskManager = Managers.getDefault();


    @Test
    void addInMemoryTaskManagerDifferentTypes() {
        Task task = new Task("Test addNewTask", "Test addNewTask description",
                taskManager.getGeneratorId(), TaskStatus.NEW);
        Epic epic = new Epic("Купить продукты", "Еда и напитки", taskManager.getGeneratorId(),
               TaskStatus.NEW);
        Subtask subtask = new Subtask("Еда", "Макароны", taskManager.getGeneratorId(), TaskStatus.NEW,
                    epic.getId());

        final int taskId = taskManager.addNewTask(task);
        final Task savedTask = taskManager.getTaskById(taskId);

        final int epicId = taskManager.addNewEpic(epic);
        final Epic savedEpic = taskManager.getEpicById(epicId);


        final List<Subtask> savedSubtask = taskManager.getSubtasksById(epicId);

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
    void NotConflictTasks() {
        Task taskGenerate = new Task("Test addNewTask", "Test addNewTask description",
                taskManager.getGeneratorId(), TaskStatus.NEW);
        Task task = new Task("Test addNewTask", "Test addNewTask description",
                1, TaskStatus.NEW);

        final int taskGenId = taskManager.addNewTask(taskGenerate);
        final Task savedTaskGen = taskManager.getTaskById(taskGenId);

        final int taskId = taskManager.addNewTask(task);
        final Task savedTask = taskManager.getTaskById(1);

        assertNotEquals(savedTask.getId(), savedTaskGen.getId(), "Идентификаторы задач конфликтуют");
    }

    @Test
    void taskFieldsNotChange() {
        Task task = new Task("Test addNewTask", "Test addNewTask description",
                taskManager.getGeneratorId(), TaskStatus.NEW);

        final int taskGenId = taskManager.addNewTask(task);
        final Task savedTask = taskManager.getTaskById(taskGenId);

        assertEquals(savedTask.getName(), task.getName(), "Имена Задач не совпадают.");
        assertEquals(savedTask.getDescription(), task.getDescription(), "Описания Задач не совпадают.");
        assertEquals(savedTask.getId(),task.getId(), "Идентификаторы задач не совпадают.");
        assertEquals(savedTask.getStatus(),task.getStatus(), "Статусы задач не совпадают.");

       assertTrue(savedTask == task, "Задачи не совпадают.");
    }

}

