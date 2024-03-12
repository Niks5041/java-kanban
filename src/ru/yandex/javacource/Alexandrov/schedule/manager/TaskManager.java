package ru.yandex.javacource.Alexandrov.schedule.manager;

import ru.yandex.javacource.Alexandrov.schedule.tasks.Epic;
import ru.yandex.javacource.Alexandrov.schedule.tasks.Subtask;
import ru.yandex.javacource.Alexandrov.schedule.tasks.Task;
import ru.yandex.javacource.Alexandrov.schedule.tasks.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int generatorId = 0;
    public HashMap<Integer, Task> tasks = new HashMap<>();
    public HashMap<Integer, Epic> epics = new HashMap<>();
    public HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values()); /// а почему такой способ лучше,€ спецом два написал способа, но с передачей в списко аргументов, пока сам не очень пон€л логику)
    }

    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    public void deleteAllSubtasks() {
        for (Epic epic : epics.values()) {
            epic.setSubtaskIds(null);             ///////////////////мы разве если его в конструкторе объ€вим, как параметр объекта, он потом не удалитс€ вместе с удаленеим сабтаски из эпиков
            updateEpicStatuses(epic.getId());   // тоесть вместо этого метода просто удалить сабтаски, или при этом их айди в эпики все равно будут жить?
        }
        subtasks.clear();
    }

    public Task getTasksById(int taskId) {
        return tasks.get(taskId);
    }

    public Epic getEpicsById(int taskId) {
        return epics.get(taskId);
    }

    public ArrayList<Subtask> getSubtasksById(int epicId) {
        ArrayList<Subtask> task = new ArrayList<>();
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return null;
        }
        for (int id : epic.getSubtaskIds()) {
            task.add(subtasks.get(id));
        }
        return task;
    }

    public int addNewTask(Task task) {
        int id = getGeneratorId();
        task.setTaskId(id);
        tasks.put(id, task);
        return id;
    }

    public int addNewEpic(Epic epic) {
        int id = getGeneratorId();
        epic.setTaskId(id);
        epics.put(id, epic);
        return id;
    }

    public Integer addNewSubtask(Subtask subtask) {
        int epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return null;
        }
        int id = getGeneratorId();
        subtask.setTaskId(id);
        subtasks.put(id, subtask);
        return id;
    }

    public void updateTasks(Task task) {
        final int id = task.getId();
        final Task savedTask = tasks.get(id);
        if (savedTask == null) {
            return;
        }
        tasks.put(id, task);
    }

    public void updateEpics(Epic epic) {
        Epic savedEpic = epics.get(epic.getId());
        if (savedEpic == null) {
            return;
        }
        savedEpic.setName(epic.getName());
        savedEpic.setDescription(epic.getDescription());
    }

    public void updateSubtasks(Subtask subtask) {
        int epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return;
        }
        int id = subtask.getId();
        Subtask savedSubtask = subtasks.get(id);
        if (savedSubtask == null) {
            return;
        }
        subtasks.put(id, subtask);
        updateEpicStatuses(epicId);
    }

    public void deleteTaskById(int taskId) {
        tasks.remove(taskId);
    }

    public void deleteEpicsById(int taskId) {
        final Epic epic = epics.remove(taskId);
        for (Integer subtaskId : epic.getSubtaskIds()) {
            subtasks.remove(subtaskId);
        }
    }

    public void deleteSubtaskById(int taskId) {
        Subtask subtask = subtasks.remove(taskId);
        if (subtask == null) {
            return;
        }
        Epic epic = epics.get(subtask.getEpicId());
        epic.setSubtaskIds(null);
        updateEpicStatuses(epic.getId());
    }

    public void updateEpicStatuses(int taskId) {
        Epic epic = epics.get(taskId);
        ArrayList<Subtask> subtasks = getSubtasksById(taskId);
        for (Subtask subtask : subtasks) {
            if (subtasks == null || subtask.getStatus().equals(TaskStatus.NEW)) {
                epic.setStatus(TaskStatus.NEW);
            } else {
                epic.setStatus(TaskStatus.IN_PROGRESS);
            }
        }
        for (Subtask subtask1 : subtasks) {
            if (subtask1.getStatus().equals(TaskStatus.DONE)) {
                    epic.setStatus(TaskStatus.DONE);
            } else {
                epic.setStatus(TaskStatus.IN_PROGRESS);
            }
        }
    }

    public int getGeneratorId() {
        return ++generatorId;
    }
}
