package ru.yandex.javacource.alexandrov.schedule.manager;

import java.io.File;

public class Managers {

    public static TaskManager getDefaultNoTest() {
        return new FileBackedTaskManager(new File("resources/task.csv"));
    }

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();          // вернул, чтоб гит хаб прошел тесты
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
