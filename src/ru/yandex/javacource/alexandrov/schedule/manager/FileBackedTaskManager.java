package ru.yandex.javacource.alexandrov.schedule.manager;

import ru.yandex.javacource.alexandrov.schedule.exceptions.ManagerSaveException;
import ru.yandex.javacource.alexandrov.schedule.tasks.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;
    private static final String HEADER = "id,type,name,status,description,epic,start,duration,finish";

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    protected void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(HEADER);
            writer.newLine();

            tasks.entrySet().forEach(entry -> {
                final Task task = entry.getValue();
                try {
                    writer.write(convertToString(task));
                    writer.newLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            subtasks.entrySet().forEach(entry -> {
                final Task task = entry.getValue();
                try {
                    writer.write(convertToString(task));
                    writer.newLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            epics.entrySet().forEach(entry -> {
                final Task task = entry.getValue();
                try {
                    writer.write(convertToString(task));
                    writer.newLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            writer.newLine();
        } catch (IOException e) {
            throw new ManagerSaveException("Can't save to file: " + file.getName(), e);
        }
    }

    private String convertToString(Task task) {
        if (task.getType().equals(TaskType.SUBTASK)) {
            Subtask subtask = (Subtask) task;
            return task.getId() + "," + task.getType() + "," + task.getName() + "," + task.getStatus() +
                    "," + task.getDescription() + "," + subtask.getEpicId() + "," + task.getStartTime() +
                    "," + task.getDuration().toMinutes() + "," + task.getEndTime();
        } else {
            return task.getId() + "," + task.getType() + "," + task.getName() + "," + task.getStatus() +
                    "," + task.getDescription() + "," + task.getStartTime() + "," + task.getDuration().toMinutes() +
                    "," + task.getEndTime();
        }
    }

    private static Task convertFromString(String value) {
        String[] split = value.split(",");

        int id = Integer.parseInt(split[0]);
        TaskType type = TaskType.valueOf(split[1]);
        String name = split[2];
        TaskStatus taskStatus = TaskStatus.valueOf(split[3]);
        String description = split[4];
        LocalDateTime startTime = LocalDateTime.parse(split[5]);
        Duration duration = Duration.ofMinutes(Long.parseLong(split[6]));

        if (type == TaskType.EPIC) {
            return new Epic(name, description, id, taskStatus, startTime, duration);
        } else if (type == TaskType.SUBTASK) {
            int epicId = Integer.parseInt(split[5]);
            return new Subtask(name, description, id, taskStatus, startTime, duration, epicId);
        } else {
            return new Task(name, description, id, taskStatus, startTime, duration);
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        final FileBackedTaskManager taskManager = new FileBackedTaskManager(file);
        try {
            final String csv = Files.readString(file.toPath());
            final String[] lines = csv.split(System.lineSeparator());
            int generatorId = 0;
            for (int i = 1; i < lines.length; i++) {
                String line = lines[i];
                if (line.isEmpty()) {
                    break;
                }
                final Task task = convertFromString(line);
                final int id = task.getId();
                if (id > generatorId) {
                    generatorId = id;
                }
                taskManager.addAnyTask(task);
            }

            taskManager.subtasks.entrySet().forEach(e -> {
                final Subtask subtask = e.getValue();
                final Epic epic = taskManager.epics.get(subtask.getEpicId());
                epic.addSubtaskId(subtask.getId());
            });

            taskManager.generatorId = generatorId;
        } catch (IOException e) {
            throw new ManagerSaveException("Can't read form file: " + file.getName(), e);
        }
        return taskManager;
    }

    protected void addAnyTask(Task task) {
        final int id = task.getId();
        switch (task.getType()) {
            case TASK:
                tasks.put(id, task);
                break;
            case SUBTASK:
                subtasks.put(id, (Subtask) task);
                break;
            case EPIC:
                epics.put(id, (Epic) task);
                break;
        }
    }

    @Override
    public void deleteAllTasks()  {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics()  {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks()  {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public int addNewTask(Task task)  {
        int taskId = super.addNewTask(task);
        save();
        return taskId;

    }

    @Override
    public int addNewEpic(Epic epic)  {
        int epicId = super.addNewEpic(epic);
        save();
        return epicId;
    }

    @Override
    public  Integer addNewSubtask(Subtask subtask)  {
        int subtaskId = super.addNewSubtask(subtask);
        save();
        return subtaskId;
    }

    @Override
    public void updateTasks(Task task)  {
        super.updateTasks(task);
        save();
    }

    @Override
    public void updateEpics(Epic epic)  {
        super.updateEpics(epic);
        save();
    }

    @Override
    public void updateSubtasks(Subtask subtask) {
        super.updateSubtasks(subtask);
        save();
    }

    @Override
    public void deleteTaskById(int id)  {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicsById(int id)  {
        super.deleteEpicsById(id);
        save();
    }

    @Override
    public void deleteSubtask(int id)  {
        super.deleteSubtask(id);
        save();
    }
}
