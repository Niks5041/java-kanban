package ru.yandex.javacource.alexandrov.schedule.tasks;

public class Subtask extends Epic {
    private int epicId;

    public Subtask(String name, String description, int taskId, TaskStatus status, int epicId) {
        super(name, description, taskId, status);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, TaskStatus status, int epicId) {               ///Тест конструктор
        super(name, description, status);
        this.epicId = epicId;
    }


    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public int setEpicIdTest(int epicId) {             //добавил для теста
        return epicId;
    }
}




