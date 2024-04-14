package ru.yandex.javacource.alexandrov.schedule.manager;

import ru.yandex.javacource.alexandrov.schedule.tasks.Task;
import ru.yandex.javacource.alexandrov.schedule.tasks.Node;

import java.util.ArrayList;

public interface HistoryManager {

    void add(Task task);

    void remove(int id);

    ArrayList<Task> getHistory();

    void linkLast(Task task);

    void removeNode(Node node);

}
