package ru.yandex.javacource.alexandrov.schedule.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.javacource.alexandrov.schedule.tasks.Epic;
import ru.yandex.javacource.alexandrov.schedule.tasks.Subtask;
import ru.yandex.javacource.alexandrov.schedule.tasks.Task;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String[] path = exchange.getRequestURI().getPath().split("/");

        switch (method) {
            case "POST":
                handlePostRequest(path, exchange);
                break;
            case "DELETE":
                handleDeleteRequest(path, exchange);
                break;
            case "GET":
                handleGetRequest(path, exchange);
                break;
        }
    }

    private void handlePostRequest(String[] path, HttpExchange exchange) throws IOException {
        switch (path[1]) {
            case "tasks":
                tasksCasePost(exchange);
                break;
            case "subtasks":
                subtasksCasePost(exchange);
                break;
            case "epics":
                epicsCasePost(exchange);
                break;
        }
    }

    private void handleDeleteRequest(String[] path, HttpExchange exchange) throws IOException {
        if (path.length == 3) {
            switch (path[1]) {
                case "tasks":
                    tasksCaseDelete(exchange, path);
                    break;
                case "subtasks":
                    subtasksCaseDelete(exchange, path);
                    break;
                case "epics":
                    epicsCaseDelete(exchange, path);
                    break;
            }
        }
    }

    private void handleGetRequest(String[] path, HttpExchange exchange) throws IOException {
        switch (path[1]) {
            case "tasks":
                tasksCaseGet(exchange, path);
                break;
            case "subtasks":
                subtasksCaseGet(exchange, path);
                break;
            case "epics":
                epicsCaseGet(exchange, path);
                break;
        }
    }

    private void tasksCaseDelete(HttpExchange ex, String[] path) {
        try {
            int id = Integer.parseInt(path[2]);
            Task task = tm.getTaskById(id);

            if (task == null) {
                sendNotFound(ex, "Задача не найдена!");
            } else {
                tm.deleteTaskById(id);
                sendText(ex, "Успех! Задача удалена!");
            }
        } catch (IOException exception) {
            System.out.println("Ошибка файла");
            exception.printStackTrace();
        }
    }

    private void tasksCasePost(HttpExchange ex) {
        try {
            String body = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Task task = getGson().fromJson(body, Task.class);

            if (task.getId() > 0) {
                tm.addNewTask(task);
                sendText(ex, "Успех! Задача создана!");
            } else if (tm.getPrioritizedTasks().contains(task)) {
                sendHasInteractions(ex, "Новая задача пересекается по времени с уже существующей :(");
            } else {
                tm.updateTasks(task);
                sendText(ex, "Успех! Задача обновлена!");
            }
        } catch (IOException exception) {
            System.out.println("Ошибка файла");
            exception.printStackTrace();
        }
    }

    private void tasksCaseGet(HttpExchange ex, String[] path) {
        try {
            String body = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Task task = getGson().fromJson(body, Task.class);

            if (path.length == 3) {
                if (task == null) {
                    sendNotFound(ex, "Задача не найдена!");
                }
                String savedTask = getGson().toJson(task);
                sendText(ex, savedTask);
            }
            if (path.length == 2) {
                List<Task> tasks = tm.getAllTasks();

                if (tasks.isEmpty()) {
                    sendNotFound(ex, "Задачи не найдены!");
                } else {
                    String jsonTasks = getGson().toJson(tasks);
                    sendText(ex, jsonTasks);
                }
            }
        } catch (IOException exception) {
            System.out.println("Ошибка файла");
            exception.printStackTrace();
        }
    }

    private void subtasksCasePost(HttpExchange ex) {
        try {
            String body = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Subtask subtask = getGson().fromJson(body, Subtask.class);

            if (subtask == null) {
                sendNotFound(ex, "Сабтаска не найдена!");
            }
            if (subtask.getId() > 0) {
                tm.addNewSubtask(subtask);
                sendText(ex, "Успех! Сабтаска создана!");
            } else if (tm.getPrioritizedTasks().contains(subtask)) {
                sendHasInteractions(ex,"Новая сабтаска пересекается по времени с уже существующей :(");
            } else {
                tm.updateSubtasks(subtask);
                sendText(ex, "Успех! Сабтаска обновлена!");
            }
        } catch (IOException exception) {
            System.out.println("Ошибка файла");
            exception.printStackTrace();
        }
    }

    private void subtasksCaseDelete(HttpExchange ex, String[] path) {
        try {
            int id = Integer.parseInt(path[2]);
            Subtask subtask = tm.getSubtaskById(id);

            if (subtask == null) {
                sendNotFound(ex, "Сабтаска не найдена!");
            } else {
                tm.deleteSubtaskById(id);
                sendText(ex, "Успех! Сабтаска удалена!");
            }
        } catch (IOException exception) {
            System.out.println("Ошибка файла");
            exception.printStackTrace();
        }
    }

    private void subtasksCaseGet(HttpExchange ex, String[] path) {
        try {
            String body = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Subtask subtask = getGson().fromJson(body, Subtask.class);

            if (path.length == 3) {
                if (subtask == null) {
                    sendNotFound(ex, "Сабтаска не найдена!");
                }
                String savedSubtask = getGson().toJson(subtask);
                sendText(ex, savedSubtask);
            }
            if (path.length == 2) {
                List<Subtask> subtasks = tm.getAllSubtasks();

                if (subtasks.isEmpty()) {
                    sendNotFound(ex, "Сабтаски не найдены!");
                } else {
                    String jsonSubtasks = getGson().toJson(subtasks);
                    sendText(ex, jsonSubtasks);
                }
            }
        } catch (IOException exception) {
            System.out.println("Ошибка файла");
            exception.printStackTrace();
        }
    }

    private void epicsCasePost(HttpExchange ex) {
        try {
            String body = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Epic epic = getGson().fromJson(body, Epic.class);

            if (epic == null) {
                sendNotFound(ex, "Эпик не найден!");
            }
            if (epic.getId() > 0) {
                tm.addNewEpic(epic);
                sendText(ex, "Успех! Эпик создан!");
            } else {
                tm.updateEpics(epic);
                sendText(ex, "Успех! Эпик обновлен!");
            }
        } catch (IOException exception) {
            System.out.println("Ошибка файла");
            exception.printStackTrace();
        }
    }

    private void epicsCaseDelete(HttpExchange ex, String[] path) {
        try {
            int id = Integer.parseInt(path[2]);
            Epic epic = tm.getEpicById(id);

            if (epic == null) {
                sendNotFound(ex, " Эпик  не найден!");
            } else {
                tm.deleteEpicsById(id);
                sendText(ex, "Успех! Эпик удален!");
            }
        } catch (IOException exception) {
            System.out.println("Ошибка файла");
            exception.printStackTrace();
        }
    }

    private void epicsCaseGet(HttpExchange ex, String[] path) {
        try {
            String body = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Epic epic = getGson().fromJson(body, Epic.class);

            if (path.length == 3) {
                if (epic == null) {
                    sendNotFound(ex, "Сабтаска не найдена!");
                }
                String savedEpic = getGson().toJson(epic);
                sendText(ex, savedEpic);
            }
            if (path.length == 2) {
                List<Epic> epics = tm.getAllEpics();

                if (epics.isEmpty()) {
                    sendNotFound(ex, "Эпики не найдены!");
                } else {
                    String jsonEpics = getGson().toJson(epics);
                    sendText(ex, jsonEpics);
                }
            }
            if (path.length == 4) {
                List<Subtask> epicSubtasks = tm.getSubtasksByEpicId(epic.getId());
                String[] savedEpicSubtasks = new String[]{getGson().toJson(epicSubtasks)};

                if (savedEpicSubtasks.length == 0) {
                    sendNotFound(ex, "Сабтаски эпика не найдены!");
                } else {
                    sendText(ex, Arrays.toString(savedEpicSubtasks));
                }
            }
        } catch (IOException exception) {
            System.out.println("Ошибка файла");
            exception.printStackTrace();
        }
    }
}
