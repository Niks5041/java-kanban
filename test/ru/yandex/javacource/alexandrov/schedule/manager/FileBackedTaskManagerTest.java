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

//    @Test
//    void testSaveAndDownloadSomeTasks() {
//        try {
//            Path tempFile = Files.createTempFile("temp", ".txt");
//            Path tempFilePath = Paths.get(tempFile.toUri());
//
//            FileBackedTaskManager taskManager = new FileBackedTaskManager(new File(tempFilePath.toString()));
//
//            List<Task> tasks = new ArrayList<>();
//            Task task = new Task("Задача 1", "Test addNewTask description", TaskStatus.NEW);
//            Epic epic = new Epic("Эпик 1", "Еда и напитки", TaskStatus.NEW);
//            tasks.add(task);
//            tasks.add(epic);
//
//            taskManager.addNewTask(task);
//            taskManager.addNewEpic(epic);
//
//            taskManager.loadFromFile(tempFilePath.toFile());
//
//            assertEquals(tasks.size(), taskManager.getAllTasks().size());
//
//        } catch (IOException e) {
//            throw new RuntimeException("Ошибка при чтении файла");
//        } catch (ManagerSaveException e) {
//            throw new RuntimeException("Ошибка при чтении файла");
//        }
//    }
}
