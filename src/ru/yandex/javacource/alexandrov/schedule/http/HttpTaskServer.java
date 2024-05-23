package ru.yandex.javacource.alexandrov.schedule.http;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private HttpServer httpServer;

    public static void main(String[] args) {
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
        //httpTaskServer.stop();
    }

    public void start() {
        try {
            httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
            httpServer.createContext("/tasks", new TaskHandler());
            httpServer.createContext("/epics", new TaskHandler());
            httpServer.createContext("/subtasks", new TaskHandler());
            httpServer.createContext("/history", new HistoryHandler());
            httpServer.createContext("/prioritized", new HistoryHandler());
            httpServer.start();
            System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        httpServer.stop(1);
        System.out.println("HTTP-сервер остановлен!");
    }
}
