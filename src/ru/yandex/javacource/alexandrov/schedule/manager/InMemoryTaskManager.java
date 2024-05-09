package ru.yandex.javacource.alexandrov.schedule.manager;

import ru.yandex.javacource.alexandrov.schedule.exceptions.TaskValidationException;
import ru.yandex.javacource.alexandrov.schedule.tasks.Epic;
import ru.yandex.javacource.alexandrov.schedule.tasks.Subtask;
import ru.yandex.javacource.alexandrov.schedule.tasks.Task;
import ru.yandex.javacource.alexandrov.schedule.tasks.TaskStatus;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected int generatorId = 0;
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    protected Set<Task> prioritizedTasks = new HashSet<>();

    @Override
    public Set<Task> getPrioritizedTasks() {
        prioritizedTasks = new TreeSet<>((Task task1, Task task2) -> {
            if (task1.getStartTime().isBefore(task2.getStartTime())) {
                return 1;
            } else if (task1.getStartTime().isAfter(task2.getStartTime())) {
                return -1;
            } else {
                return 0;
            }
        });
        prioritizedTasks.addAll(tasks.values());
        prioritizedTasks.addAll(epics.values());
        prioritizedTasks.addAll(subtasks.values());
        return prioritizedTasks;
    }

    @Override
    public boolean checkValidation(Task task) {
        if (prioritizedTasks.isEmpty()) {
            return false;
        }
        Optional<LocalDateTime> maxStartTime = prioritizedTasks.stream()
                .map(Task::getStartTime)
                .max(Comparator.comparing(taskStream -> taskStream));

        return task.getEndTime().isAfter(maxStartTime.get());
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        epics.values().forEach(epic -> {
            epic.cleanSubtaskIds();
            updateEpicStatuses(epic.getId());
        });
        subtasks.clear();
    }

    @Override
    public Task getTaskById(int taskId) {
        Task task = tasks.get(taskId);
        historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpicById(int taskId) {
        Epic epic = epics.get(taskId);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public List<Subtask> getSubtasksByEpicId(int epicId) {
        List<Subtask> task = new ArrayList<>();
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return null;
        }
        epic.getSubtaskIds().forEach(id -> task.add(subtasks.get(id)));
        return task;
    }

    @Override
    public int addNewTask(Task task) {
        boolean validation = tasks.values().stream()
                .anyMatch(existingTask -> checkValidation(task));
        if (validation) {
            throw new TaskValidationException("Новая задача пересекается с существующими задачами");
        }
        int id = ++generatorId;
        task.setTaskId(id);
        tasks.put(id, task);
        return id;
    }

    @Override
    public int addNewEpic(Epic epic) {
        boolean validation = tasks.values().stream()
                .anyMatch(existingTask -> checkValidation(epic));
        if (validation) {
            throw new TaskValidationException("Новая задача пересекается с существующими задачами");
        }
        int id = ++generatorId;
        epic.setTaskId(id);
        epics.put(id, epic);
        return id;
    }

    @Override
    public Integer addNewSubtask(Subtask subtask) {
        boolean validation = tasks.values().stream()
                .anyMatch(existingTask -> checkValidation(subtask));
        if (validation) {
            throw new TaskValidationException("Новая задача пересекается с существующими задачами");
        }
        int epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return null;
        }
        int id = ++generatorId;
        subtask.setTaskId(id);
        subtasks.put(id, subtask);
        epic.addSubtaskId(subtask.getId());
        updateEpicStatuses(epicId);
        return id;
    }

    @Override
    public void updateTasks(Task task) {
        final int id = task.getId();
        final Task savedTask = tasks.get(id);
        if (savedTask == null) {
            return;
        }
        tasks.put(id, task);
    }

    @Override
    public void updateEpics(Epic epic) {
        Epic savedEpic = epics.get(epic.getId());
        if (savedEpic == null) {
            return;
        }
        epic.setSubtaskIds(savedEpic.getSubtaskIds());
        epic.setStatus(savedEpic.getStatus());
        epics.put(epic.getId(), epic);
    }

    @Override
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

    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteEpicsById(int id) {
        final Epic epic = epics.remove(id);
        historyManager.remove(id);
        epic.getSubtaskIds().forEach(subtaskId -> {
            subtasks.remove(subtaskId);
            historyManager.remove(subtaskId);
        });
    }

    @Override
    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.remove(id);
        historyManager.remove(id);
        if (subtask == null) {
            return;
        }
        Epic epic = epics.get(subtask.getEpicId());
        epic.removeSubtask(id);
        updateEpicStatuses(epic.getId());
    }

    private void updateEpicStatuses(int taskId) {
        Epic epic = epics.get(taskId);
        List<Subtask> subtasks = getSubtasksByEpicId(taskId);
        subtasks.forEach(subtask -> {
            if (subtasks == null || subtask.getStatus().equals(TaskStatus.NEW)) {
                epic.setStatus(TaskStatus.NEW);
            } else {
                epic.setStatus(TaskStatus.IN_PROGRESS);
            }
        });
        subtasks.forEach(subtask1 -> {
            if (subtask1.getStatus().equals(TaskStatus.DONE)) {
                epic.setStatus(TaskStatus.DONE);
            } else {
                epic.setStatus(TaskStatus.IN_PROGRESS);
            }
        });
    }

    @Override
    public List<Task> getHistoryManager() {
        return historyManager.getHistory();
    }
}

