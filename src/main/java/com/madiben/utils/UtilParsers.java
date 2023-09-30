package com.madiben.utils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Clase de utilidades para parsear
 */
public class UtilParsers {


    private static UtilParsers utilParsersInstance;

    private UtilParsers() {
    }

    /**
     * Obtiene la instancia de UtilParsers
     * SINGLETON
     *
     * @return Instancia de UtilParsers
     */
    public static UtilParsers getInstance() {
        if (utilParsersInstance == null) {
            utilParsersInstance = new UtilParsers();
        }
        return utilParsersInstance;
    }

    /**
     * Dado un nombre de fichero, devuelve la fecha que contiene
     *
     * @param fileName Nombre del fichero
     * @return Fecha del fichero
     */
    public LocalDate parseFileNameToDate(String fileName) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return LocalDate.parse(fileName.replaceAll("\\D", ""), formatter);
    }

    /**
     * Dado un nombre de fichero, devuelve la hora que contiene
     *
     * @param timeStr Nombre del fichero
     * @return Hora del fichero
     */
    public static LocalTime parseLocalTime(String timeStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
        return LocalTime.parse(timeStr, formatter);
    }
}
