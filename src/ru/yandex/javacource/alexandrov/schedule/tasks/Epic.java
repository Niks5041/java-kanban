package ru.yandex.javacource.alexandrov.schedule.tasks;

import ru.yandex.javacource.alexandrov.schedule.manager.InMemoryTaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subtaskIds = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String name, String description, int taskId, TaskStatus status) {
        super(name, description, taskId, status);
    }

    public Epic(String name, String description, TaskStatus status) {
        super(name, description, status);
    }

    public Epic(String name, String description, TaskStatus status, LocalDateTime startTime,
                Duration duration) {
        super(name, description, status, startTime, duration);
    }

    public Epic(String name, String description, int id, TaskStatus status, LocalDateTime startTime,
                Duration duration) {
        super(name, description, id, status, startTime, duration);
    }

    @Override
    public LocalDateTime getEndTime() {
          InMemoryTaskManager taskManager = new InMemoryTaskManager();
          LocalDateTime latestEndTime = getStartTime();
          return subtaskIds.stream()
                  .map(taskManager::getSubtaskById)
                  .map(Subtask::getEndTime)
                  .max(LocalDateTime::compareTo)
                  .orElse(latestEndTime);
      }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void setSubtaskIds(ArrayList<Integer> subtaskIds) {
        this.subtaskIds = subtaskIds;
    }

    public void cleanSubtaskIds() {
        subtaskIds.clear();
    }

    public void removeSubtask(int id) {
        subtaskIds.remove(Integer.valueOf(id));
    }

    public void addSubtaskId(int subtaskId) {
        subtaskIds.add(subtaskId);
    }

    public int addSubtaskIdTest(int subtaskId) {
        return subtaskId;
    }
}



