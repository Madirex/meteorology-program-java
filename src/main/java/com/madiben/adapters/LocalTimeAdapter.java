package com.madiben.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Clase LocalTimeAdapter que adapta la clase LocalTime
 */
public class LocalTimeAdapter extends TypeAdapter<LocalTime> {
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_TIME;

    /**
     * Método de escritura
     *
     * @param out   JsonWriter
     * @param value LocalDate
     * @throws IOException Excepción de escritura
     */
    @Override
    public void write(JsonWriter out, LocalTime value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(formatter.format(value));
        }
    }

    /**
     * Método de lectura
     *
     * @param in JsonReader
     * @return LocalDate
     * @throws IOException Excepción de lectura
     */
    @Override
    public LocalTime read(JsonReader in) throws IOException {
        if (in.peek() == null) {
            return null;
        }
        String timeStr = in.nextString();
        return LocalTime.parse(timeStr, formatter);
    }
}