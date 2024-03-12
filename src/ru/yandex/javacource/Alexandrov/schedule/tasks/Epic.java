package ru.yandex.javacource.Alexandrov.schedule.tasks;

import java.util.ArrayList;
public class Epic extends Task {

    private ArrayList<Integer> subtaskIds = new ArrayList<>();

    public Epic(String name, String description, int taskId, TaskStatus status) {
        super(name, description, taskId, status);
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void setSubtaskIds(ArrayList<Integer> subtaskIds) {
        this.subtaskIds = subtaskIds;
    }
}



