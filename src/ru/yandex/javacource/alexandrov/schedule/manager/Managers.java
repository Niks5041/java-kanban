package ru.yandex.javacource.alexandrov.schedule.manager;

import java.io.File;

public class Managers {

    public static TaskManager getDefaultFileWork() {
        return new FileBackedTaskManager(new File("resources/task.csv"));
    }

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
