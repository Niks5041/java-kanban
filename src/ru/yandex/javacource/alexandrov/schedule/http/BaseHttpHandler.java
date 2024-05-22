package ru.yandex.javacource.alexandrov.schedule.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.sun.net.httpserver.HttpExchange;
import ru.yandex.javacource.alexandrov.schedule.manager.Managers;
import ru.yandex.javacource.alexandrov.schedule.manager.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BaseHttpHandler {
    TaskManager tm = Managers.getDefault();
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDataTimeAdapter())
            .create();

    protected void sendText(HttpExchange h, String text) throws IOException {
       writeResponse(h, text,200);
    }

    protected void sendNotFound(HttpExchange h, String text) throws IOException {
        writeResponse(h, text,404);
    }

    protected void sendHasInteractions(HttpExchange h, String text) throws IOException {
        writeResponse(h, text,406);
    }

    private void writeResponse(HttpExchange he, String text, int responseCode) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        he.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        he.sendResponseHeaders(responseCode, resp.length);
        he.getResponseBody().write(resp);
        he.close();
    }

   private class DurationAdapter extends TypeAdapter<Duration> {

        @Override
        public void write(final JsonWriter jsonWriter, final Duration duration) throws IOException {
            jsonWriter.value(duration.toString());
        }

        @Override
        public Duration read(final JsonReader jsonReader) throws IOException {
            return Duration.parse(jsonReader.nextString());
        }
    }

    private class LocalDataTimeAdapter extends TypeAdapter<LocalDateTime> {
        private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        @Override
        public void write(final JsonWriter jsonWriter, final LocalDateTime localDateTime) throws IOException {
            jsonWriter.value(localDateTime.format(dtf));
        }

        @Override
        public LocalDateTime read(final JsonReader jsonReader) throws IOException {
            return LocalDateTime.parse(jsonReader.nextString(), dtf);
        }
    }

    public Gson getGson() {
        return gson;
    }
}