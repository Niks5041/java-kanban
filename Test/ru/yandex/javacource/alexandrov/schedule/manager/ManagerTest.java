package ru.yandex.javacource.alexandrov.schedule.manager;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ManagerTest {

    @Test
    void managerReturnObjects() {

        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();

        assertNotNull(taskManager,"Объект не найден.");
        assertNotNull(historyManager,"Объект не найден.");
    }
}
