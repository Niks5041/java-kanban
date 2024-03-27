package ru.yandex.javacource.alexandrov.schedule.manager;


import org.junit.jupiter.api.Test;
import ru.yandex.javacource.alexandrov.schedule.tasks.Task;
import ru.yandex.javacource.alexandrov.schedule.tasks.TaskStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryHistoryManagerTest {

    TaskManager taskManager = Managers.getDefault();
    HistoryManager historyManager = Managers.getDefaultHistory();

    @Test
    void taskNotChange() {
        Task task = new Task("Задача 1", "Test addNewTask description",
                0, TaskStatus.NEW);

        historyManager.add(task);

        List<Task> taskHistory = historyManager.getHistory();

        assertEquals("Задача 1", taskHistory.get(0).getName(),
                "Название первой версии задачи должно быть 'Задача 1'");
    }
}




