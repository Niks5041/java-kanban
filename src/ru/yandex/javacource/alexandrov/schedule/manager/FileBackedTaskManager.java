package ru.yandex.javacource.alexandrov.schedule.manager;

import ru.yandex.javacource.alexandrov.schedule.exceptions.ManagerSaveException;
import ru.yandex.javacource.alexandrov.schedule.tasks.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private static Task CSVTaskFormat;                 // почему именно так? не понимаю что с этим делать и тест не проходит
    private final File file;                                   //а почему вы на File меняете, Я так понял, что Path выйгрышнее на сегодняшний день
    private static final String HEADER = "id,type,name,status,description,epic";

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    protected void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(HEADER);
            writer.newLine();

            for (Map.Entry<Integer, Task> entry : tasks.entrySet()) {
                final Task task = entry.getValue();
                writer.write(CSVTaskFormat.toString(task));
                writer.newLine();
            }

            for (Map.Entry<Integer, Subtask> entry : subtasks.entrySet()) {
                final Task task = entry.getValue();
                writer.write(CSVTaskFormat.toString(task));
                writer.newLine();
            }

            for (Map.Entry<Integer, Epic> entry : epics.entrySet()) {
                final Task task = entry.getValue();
                writer.write(CSVTaskFormat.toString(task));
                writer.newLine();
            }

            writer.newLine();
        } catch (IOException e) {
            throw new ManagerSaveException("Can't save to file: " + file.getName(), e);
        }
//        try (BufferedWriter bW = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
//            bW.write("id,type,name,status,description,epic" + "\n");
//            for (Task task : super.getAllTasks()) {
//                String taskString = convertToString(task);
//                bW.write(taskString + "\n");
//            }                                                         я проверял в дебагере, у меня и тип и все остальное учитывалось, почему так плохо?
                                                                       // а если пустой хэшмэп будет, ничего разве?
    }

    private String convertToString(Task task) {
        return task.getId() + "," + task.getType() + "," + task.getName() + "," + task.getStatus() + "," + task.getDescription() + "," + (task.getType().equals(TaskType.SUBTASK) ? task.getId() : ""); // У такси нет такого метода, только ранее получить объект сабтаски, но там громоздко получается опять же
    } // я поэтому и намудрил тот код, чтобы получить епик айди у сабтаски

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

    public static FileBackedTaskManager loadFromFile(File file) {                          //а почему вы на File меняете, Я так понял, что Path выйгрышнее на сегодняшний день
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
                taskManager.addNewTask(task);                     //вы хотите, чтоб я метод универсальный создал? taskManager.addAnyTask(task);
            }
            for (Map.Entry<Integer, Subtask> e : taskManager.subtasks.entrySet()) {
                final Subtask subtask = e.getValue();
                final Epic epic = taskManager.epics.get(subtask.getEpicId());
                epic.addSubtaskId(subtask.getId());
            }
            taskManager.generatorId = generatorId;
        } catch (IOException e) {
            throw new ManagerSaveException("Can't read form file: " + file.getName(), e);
        }
        return taskManager;
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
