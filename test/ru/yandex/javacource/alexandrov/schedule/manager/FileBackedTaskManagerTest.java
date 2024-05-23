package ru.yandex.javacource.alexandrov.schedule.manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileBackedTaskManagerTest  {

     @Test
    public void testException() {

        File tempFile = null;
        try {
            tempFile = File.createTempFile("tempfile", ".txt");

            FileWriter writer = new FileWriter(tempFile);
            writer.write("1,TASK,Task1,TODO,Description1,2024-05-12T10:00:00,60,2024-05-12T11:00:00");
            writer.write("2,EPIC,Epic1,IN_PROGRESS,Description2,2024-05-12T12:00:00,120,2024-05-12T14:00:00");
            writer.close();

            FileBackedTaskManager tm = FileBackedTaskManager.loadFromFile(tempFile);

            Assertions.assertDoesNotThrow(() -> tm.save());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

