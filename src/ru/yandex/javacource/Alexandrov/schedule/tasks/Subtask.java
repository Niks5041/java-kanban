package ru.yandex.javacource.Alexandrov.schedule.tasks;

import java.util.ArrayList;
public class Subtask extends Epic {
    private int epicId;

    public Subtask(String name, String description, int taskId, TaskStatus status, int epicId) {
        super(name, description, taskId, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }
}




