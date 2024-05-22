package ru.yandex.javacource.alexandrov.schedule.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.javacource.alexandrov.schedule.tasks.Task;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
    private final Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String[] path = exchange.getRequestURI().getPath().split("/");

        switch (method) {
            case "GET":
                switch (path[1]) {
                    case "history":
                        historyCaseGet(exchange);
                        break;
                    case "prioritized":
                        prioritizedCaseGet(exchange);
                        break;
                }
                break;
        }
    }

    private void historyCaseGet(HttpExchange ex) throws IOException {
        try {
            List<Task> history = tm.getHistoryManager();

            if (history == null) {
                sendNotFound(ex, "История пустая!");
            }
            String[] savedHistory = new String[]{getGson().toJson(history)};
            sendText(ex, Arrays.toString(savedHistory));
        } catch (IOException exception) {
            System.out.println("Ошибка файла");
            exception.printStackTrace();
        }
    }

    private void prioritizedCaseGet(HttpExchange ex) throws IOException {
        try {
            List<Task> history = tm.getHistoryManager();

            if (history == null) {
                sendNotFound(ex, "История пустая!");
            }
            String[] savedHistory = new String[]{getGson().toJson(history)};
            sendText(ex, Arrays.toString(savedHistory));
        } catch (IOException exception) {
            System.out.println("Ошибка файла");
            exception.printStackTrace();
        }
    }
}

