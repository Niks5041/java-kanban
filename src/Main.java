import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Task task1 = new Task("Сделать уроки","Java", taskManager.generateTaskId(), TaskStatus.NEW);
        Task task2 = new Task("Прибраться", "Помыть посуду", taskManager.generateTaskId(), TaskStatus.NEW);

        ArrayList<Subtask> epicTaskWithSubtask1 = new ArrayList<>();
        Subtask subtask1 = new Subtask("Еда", "Макароны", taskManager.generateSubtaskId(),
                TaskStatus.NEW, epicTaskWithSubtask1);
        Subtask subtask2 = new Subtask("Напитки", "Сок", taskManager.generateSubtaskId(),
                TaskStatus.NEW, epicTaskWithSubtask1);
        epicTaskWithSubtask1.add(subtask1);
        epicTaskWithSubtask1.add(subtask2);
        Epic epicTask1 = new Epic("Купить продукты", "Еда и Напитки", taskManager.generateEpicId(),
                TaskStatus.NEW, epicTaskWithSubtask1);

        ArrayList<Subtask> epicTaskWithSubtask2 = new ArrayList<>();
        Subtask subtask3 = new Subtask("Убрать в шкафу", "Разобрать одежду", taskManager.generateSubtaskId(),
                TaskStatus.NEW, epicTaskWithSubtask2);
        epicTaskWithSubtask2.add(subtask3);
        Epic epicTask2 = new Epic("Уборка", "По ситуации", taskManager.generateEpicId(),
                TaskStatus.NEW, epicTaskWithSubtask2);

        taskManager.createNewTask(task1);
        taskManager.createNewTask(task2);

        taskManager.createNewEpicTask(epicTask1);
        taskManager.createNewEpicTask(epicTask2);

        taskManager.createNewEpicSubTask(subtask1);
        taskManager.createNewEpicSubTask(subtask2);
        taskManager.createNewEpicSubTask(subtask3);

        System.out.println("Список задач");
        System.out.println(taskManager.getAllTasks());
        System.out.println("");
        System.out.println("Список эпиков");
        System.out.println(taskManager.getAllEpicTasks());
        System.out.println("");
        System.out.println("Список подзадач");
        System.out.println(taskManager.getAllEpicSubtasks());
        System.out.println("");

        task1.setStatus(TaskStatus.IN_PROGRESS);
        subtask1.setStatus(TaskStatus.DONE);
        subtask2.setStatus(TaskStatus.DONE);
        taskManager.updateTaskStatuses(1);
        taskManager.updateEpicStatuses(1);

        System.out.println("Обновленная задача 1:");
        System.out.println(task1);
        System.out.println("");
        System.out.println("Обновленный Эпик 1:");
        System.out.println(epicTask1);
        System.out.println("");

        System.out.println("Задачи до удаления: ");
        System.out.println(taskManager.tasks);
        taskManager.deleteTaskById(1);
        System.out.println(" ");
        System.out.println("Задачи после удаления: ");
        System.out.println(taskManager.tasks);
        System.out.println(" ");

        System.out.println("Эпики до удаления: ");
        System.out.println(taskManager.epicTasks);
        System.out.println(" ");
        taskManager.deleteEpicTaskById(1);;
        System.out.println("Эпики после удаления: ");
        System.out.println(taskManager.epicTasks);
        System.out.println(" ");
    }
}
