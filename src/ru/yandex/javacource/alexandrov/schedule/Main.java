package ru.yandex.javacource.alexandrov.schedule;


import ru.yandex.javacource.alexandrov.schedule.manager.Managers;
import ru.yandex.javacource.alexandrov.schedule.manager.TaskManager;
import ru.yandex.javacource.alexandrov.schedule.tasks.Subtask;
import ru.yandex.javacource.alexandrov.schedule.tasks.Task;
import ru.yandex.javacource.alexandrov.schedule.tasks.Epic;
import ru.yandex.javacource.alexandrov.schedule.tasks.TaskStatus;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefault();

        Task task1 = new Task("Сделать уроки","Java", TaskStatus.NEW);
        Task task2 = new Task("Прибраться", "Помыть посуду", TaskStatus.NEW);

        Epic epicTask1 = new Epic("Купить продукты", "Еда и Напитки",
                TaskStatus.NEW);
        Epic epicTask2 = new Epic("Уборка", "По ситуации",
                TaskStatus.NEW);

        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);

        taskManager.addNewEpic(epicTask1);
        Subtask subtask1 = new Subtask("Еда", "Макароны",  TaskStatus.NEW,
                epicTask1.getId());
        Subtask subtask2 = new Subtask("Напитки", "Сок",  TaskStatus.NEW,
                epicTask1.getId());
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);

        taskManager.addNewEpic(epicTask2);
        Subtask subtask3 = new Subtask("Убрать в шкафу", "Разобрать одежду",
                TaskStatus.NEW, epicTask2.getId());
        taskManager.addNewSubtask(subtask3);

        printAllTasks(taskManager);
   }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getAllEpics()) {
            System.out.println(epic);

            for (Task task : manager.getSubtasksByEpicId(epic.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getAllSubtasks()) {
            System.out.println(subtask);
        }
        System.out.println("История:");
        for (Task task : manager.getHistoryManager()) {
            System.out.println(task);
        }
    }
}
