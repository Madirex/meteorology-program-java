package com.madirex.utils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class UtilParsers {


    private static UtilParsers utilParsersInstance;

    private UtilParsers() {
    }

    public static UtilParsers getInstance() {
        if (utilParsersInstance == null) {
            utilParsersInstance = new UtilParsers();
        }
        return utilParsersInstance;
    }

    public LocalDate parseFileNameToDate(String fileName) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return LocalDate.parse(fileName.replaceAll("\\D", ""), formatter);
    }

    public static LocalTime parseLocalTime(String timeStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
        try {
            return LocalTime.parse(timeStr, formatter);
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Error al analizar la hora: " + timeStr, e);
        }
    }
}
