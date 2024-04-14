package ru.yandex.javacource.alexandrov.schedule.manager;

import ru.yandex.javacource.alexandrov.schedule.tasks.Task;
import ru.yandex.javacource.alexandrov.schedule.tasks.Node;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node> history = new HashMap<>();
    private Node head;
    private Node tail;
    private int size = 0;

    @Override
    public void add(Task task) {
        int id = task.getId();
        if (history.containsKey(id)) {
            Node node = history.get(id);
            removeNode(node);
        }
        Node newNode = new Node(null, task, null);

        if (tail != null) {
            tail.next = newNode;
            newNode.prev = tail;
        }
        tail = newNode;

        if (head == null) {
            head = newNode;
        }

        history.put(id, newNode);
        size++;
    }

    @Override
    public void remove(int id) {
        Node node = history.get(id);
        removeNode(node);
        history.remove(id);
    }

    @Override
    public ArrayList<Task> getHistory() {
        ArrayList<Task> hist = new ArrayList<>();
        Node current = head;
        while (current != null) {
            hist.add((Task) current.task);
            current = current.next;
        }
        return hist;
    }

    @Override
    public void linkLast(Task task) {
        final Node<Task> t = tail;
        final Node<Task> newNode = new Node<>(t, task, null);
        tail = newNode;
        if (t == null)
            head = newNode;
        else
            tail.next = newNode;
        size++;
    }

    @Override
    public void removeNode(Node node) {
        if (node == null) {
            return;
        }

        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }

        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }
        size--;
    }
}


