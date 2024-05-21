package ru.yandex.javacource.alexandrov.schedule.manager;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTaskManagerTest {
    @Test
    void testSaveAndLoadEmptyFile() {
        try {
            Path tempFile = Files.createTempFile("temp", ".txt");
            Path tempFilePath = Paths.get(tempFile.toUri());

            FileBackedTaskManager taskManager = new FileBackedTaskManager(new File(tempFilePath.toString()));
            taskManager.save();

            taskManager.loadFromFile(tempFilePath.toFile());

            assertEquals(0, taskManager.getAllTasks().size());

        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }


}
