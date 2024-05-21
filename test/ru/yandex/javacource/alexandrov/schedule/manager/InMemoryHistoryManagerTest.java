package ru.yandex.javacource.alexandrov.schedule.manager;


import org.junit.jupiter.api.Test;
import ru.yandex.javacource.alexandrov.schedule.tasks.Task;
import ru.yandex.javacource.alexandrov.schedule.tasks.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryHistoryManagerTest {
       HistoryManager historyManager = Managers.getDefaultHistory();

        @Test
        public void testAdd() {
            Task task1 = new Task("Задача 1", "Описание 1",1, TaskStatus.NEW,
                    LocalDateTime.of(2024, 1, 01, 00, 00), Duration.ofMinutes(10));
            Task task2 = new Task("Задача 2", "Описание 2",2, TaskStatus.NEW,
                    LocalDateTime.of(2024, 2, 01, 00, 00), Duration.ofMinutes(10));

            historyManager.add(task1);
            historyManager.add(task2);

            List<Task> history = historyManager.getHistory();

            assertEquals(2, history.size());
            assertEquals(task1, history.get(0));
            assertEquals(task2, history.get(1));
        }

        @Test
        public void testDuplicateAdd() {
            Task task1 = new Task("Задача 1", "Описание 1",1, TaskStatus.NEW,
                    LocalDateTime.of(2024, 1, 01, 00, 00), Duration.ofMinutes(10));

            historyManager.add(task1);
            historyManager.add(task1);

            List<Task> history = historyManager.getHistory();

            assertEquals(1, history.size());
            assertEquals(task1, history.get(0));
        }

        @Test
        public void testRemove() {
            Task task1 = new Task("Задача 1", "Описание 1",1, TaskStatus.NEW,
                    LocalDateTime.of(2024, 1, 01, 00, 00), Duration.ofMinutes(10));
            Task task2 = new Task("Задача 2", "Описание 2",2, TaskStatus.NEW,
                    LocalDateTime.of(2024, 2, 01, 00, 00), Duration.ofMinutes(10));

            historyManager.add(task1);
            historyManager.add(task2);

            historyManager.remove(1);

            List<Task> history = historyManager.getHistory();

            assertEquals(1, history.size());
            assertEquals(task2, history.get(0));
        }

        @Test
        public void testRemoveFromEmptyHistory() {

            historyManager.remove(1);

            List<Task> history = historyManager.getHistory();

            assertEquals(0, history.size());
        }
    }






