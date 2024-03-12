package ru.yandex.javacource.Alexandrov.schedule;

import ru.yandex.javacource.Alexandrov.schedule.manager.TaskManager;
import ru.yandex.javacource.Alexandrov.schedule.tasks.Epic;
import ru.yandex.javacource.Alexandrov.schedule.tasks.Subtask;
import ru.yandex.javacource.Alexandrov.schedule.tasks.Task;
import ru.yandex.javacource.Alexandrov.schedule.tasks.TaskStatus;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Task task1 = new Task("Сделать уроки","Java", taskManager.getGeneratorId(), TaskStatus.NEW);
        Task task2 = new Task("Прибраться", "Помыть посуду", taskManager.getGeneratorId(), TaskStatus.NEW);

        Subtask subtask1 = new Subtask("Еда", "Макароны", taskManager.getGeneratorId(), TaskStatus.NEW,
                taskManager.getGeneratorId());
        Subtask subtask2 = new Subtask("Напитки", "Сок", taskManager.getGeneratorId(), TaskStatus.NEW,
                taskManager.getGeneratorId());
        Epic epicTask1 = new Epic("Купить продукты", "Еда и Напитки", taskManager.getGeneratorId(),
                TaskStatus.NEW);


        Subtask subtask3 = new Subtask("Убрать в шкафу", "Разобрать одежду", taskManager.getGeneratorId(),
                TaskStatus.NEW, taskManager.getGeneratorId());
        Epic epicTask2 = new Epic("Уборка", "По ситуации", taskManager.getGeneratorId(),
                TaskStatus.NEW);

        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);

        taskManager.addNewEpic(epicTask1);
        taskManager.addNewEpic(epicTask2);

        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        taskManager.addNewSubtask(subtask3);

        System.out.println("Список задач");
        System.out.println(taskManager.getAllTasks());
        System.out.println("");
        System.out.println("Список эпиков");
        System.out.println(taskManager.getAllEpics());
        System.out.println("");
        System.out.println("Список подзадач");
        System.out.println(taskManager.getAllSubtasks());
        System.out.println("");

        task1.setStatus(TaskStatus.IN_PROGRESS);
        subtask1.setStatus(TaskStatus.DONE);
        subtask2.setStatus(TaskStatus.DONE);
        taskManager.updateEpicStatuses(1);

        System.out.println("Обновленная задача 1:");
        System.out.println(task1);
        System.out.println("");
        System.out.println("Обновленный Эпик 1:");
        System.out.println(epicTask1);
        System.out.println("");

        System.out.println("Задачи до удаления:: ");
        System.out.println(taskManager.tasks);
        taskManager.deleteTaskById(1);
        System.out.println(" ");
        System.out.println("Задачи после удаления: ");
        System.out.println(taskManager.tasks);
        System.out.println(" ");

        System.out.println("Эпики до удаления:: ");
        System.out.println(taskManager.epics);
        System.out.println(" ");
        taskManager.deleteEpicsById(1);;
        System.out.println("Эпики после удаления: ");
        System.out.println(taskManager.epics);
        System.out.println(" ");
    }
}
