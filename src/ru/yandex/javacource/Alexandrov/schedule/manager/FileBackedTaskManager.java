package ru.yandex.javacource.alexandrov.schedule.manager;

import ru.yandex.javacource.alexandrov.schedule.exceptions.ManagerSaveException;
import ru.yandex.javacource.alexandrov.schedule.tasks.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private static Task task;
    private final Path filePath;

    public FileBackedTaskManager(String filePathLink) {
        this.filePath = Paths.get(filePathLink);
    }

    public void save() throws ManagerSaveException {
        try (BufferedWriter bW = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
            bW.write("id,type,name,status,description,epic" + "\n");
            for (Task task : super.getAllTasks()) {
                String taskString = convertToString(task);
                bW.write(taskString + "\n");
            }
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка записи задачи в файл");
        }
    }

    private String convertToString(Task task) {
        TaskType taskType;
        if (task instanceof Epic) {
            taskType = TaskType.EPIC;
        } else if (task instanceof Subtask) {
            taskType = TaskType.SUBTASK;
        } else {
            taskType = TaskType.TASK;
        }
        String id = String.valueOf(task.getId());
        String name = task.getName();
        String description = task.getDescription();
        String status = String.valueOf(task.getStatus());

        if (task instanceof Subtask) {
            Subtask subtask = (Subtask) task;
            int epicId = subtask.getEpicId();
            return id + "," + taskType + "," + name + "," + status + "," + description + "," + epicId;
        }
        return id + "," + taskType + "," + name + "," + status + "," + description;
    }

    private static Task convertFromString(String value) {
        String[] split = value.split(",");

        int id = Integer.parseInt(split[0]);
        TaskType type = TaskType.valueOf(split[1]);
        String name = split[2];
        TaskStatus taskStatus = TaskStatus.valueOf(split[3]);
        String description = split[4];

        if (type == TaskType.EPIC) {
            return new Epic(name, description, taskStatus);
        } else if (type == TaskType.SUBTASK) {
            int epicId = Integer.parseInt(split[5]);
            return new Subtask(name, description, taskStatus, epicId);
        } else {
            return new Task(name, description, taskStatus);
        }
    }

     public FileBackedTaskManager loadFromFile(Path path) throws ManagerSaveException {
         try (FileReader reader = new FileReader(String.valueOf(path), StandardCharsets.UTF_8);
              BufferedReader br = new BufferedReader(reader)) {
            br.readLine();
            while (br.ready()) {
                String line = br.readLine();
                Task savedTask = convertFromString(line);
                if (savedTask instanceof Epic) {
                    super.addNewEpic((Epic) savedTask);
                } else if (savedTask instanceof Subtask) {
                    super.addNewSubtask((Subtask) savedTask);
                } else {
                    super.addNewTask(savedTask);
                }
            }
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка при чтении файла");
        }
        return new FileBackedTaskManager(path.toString());
    }

    @Override
    public void deleteAllTasks() throws ManagerSaveException {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() throws ManagerSaveException {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() throws ManagerSaveException {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public int addNewTask(Task task) throws ManagerSaveException {
        FileBackedTaskManager.task = task;
        int taskId = super.addNewTask(task);
        save();
        return taskId;

    }

    @Override
    public int addNewEpic(Epic epic) throws ManagerSaveException {
        int epicId = super.addNewEpic(epic);
        save();
        return epicId;
    }

    @Override
    public  Integer addNewSubtask(Subtask subtask) throws ManagerSaveException {
        int subtaskId = super.addNewSubtask(subtask);
        save();
        return subtaskId;
    }

    @Override
    public void updateTasks(Task task) throws ManagerSaveException {
        super.updateTasks(task);
        save();
    }

    @Override
    public void updateEpics(Epic epic) throws ManagerSaveException {
        super.updateEpics(epic);
        save();
    }

    @Override
    public void updateSubtasks(Subtask subtask) throws ManagerSaveException {
        super.updateSubtasks(subtask);
        save();
    }

    @Override
    public void deleteTaskById(int id) throws ManagerSaveException {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicsById(int id) throws ManagerSaveException {
        super.deleteEpicsById(id);
        save();
    }

    @Override
    public void deleteSubtask(int id) throws ManagerSaveException {
        super.deleteSubtask(id);
        save();
    }
}
