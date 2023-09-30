package com.madiben.services.io;

import com.madiben.exceptions.ReadCSVFailException;
import com.madiben.models.MeteorologyData;
import com.madiben.utils.UtilParsers;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase CsvManager que administra la exportación e importación de datos CSV
 */
public class CsvManager {

    private static CsvManager csvManagerInstance;

    /**
     * Constructor privado para evitar la creación de instancia
     * SINGLETON
     */
    private CsvManager() {
    }

    /**
     * Obtiene la instancia de CsvManager
     * SINGLETON
     *
     * @return Instancia de CsvManager
     */
    public static CsvManager getInstance() {
        if (csvManagerInstance == null) {
            csvManagerInstance = new CsvManager();
        }
        return csvManagerInstance;
    }


    /**
     * Dado una path donde se ubica la carpeta de los archivos CSV de meteorología,
     * devuelve una lista de MeteorologyData con todos los datos de los diferentes archivos
     *
     * @param path Ruta de la carpeta donde están los archivos de meteorología
     * @return Lista de MeteorologyData con todos los datos de los diferentes archivos
     */
    public List<MeteorologyData> folderDataToMeteorologyList(String path) throws ReadCSVFailException {
        List<MeteorologyData> totalData = new ArrayList<>();
        try (DirectoryStream<Path> directoryStream = java.nio.file.Files.newDirectoryStream(Paths.get(path))) {
            for (Path filePath : directoryStream) {
                if (java.nio.file.Files.isRegularFile(filePath)) {
                    String fileName = filePath.getFileName().toString();
                    totalData.addAll(fileToMeteorologyDataList(path, fileName));
                }
            }
        } catch (IOException e) {
            throw new ReadCSVFailException(e.getMessage());
        }
        return totalData;
    }

    /**
     * Dado una path donde se ubica el archivo CSV de meteorología,
     * devuelve una lista de MeteorologyData con todos los datos del archivo
     *
     * @param path     Ruta de la carpeta donde está el archivo de meteorología
     * @param fileName Nombre del archivo de meteorología
     */
    public List<MeteorologyData> fileToMeteorologyDataList(String path, String fileName) throws ReadCSVFailException {
        LocalDate date = UtilParsers.getInstance().parseFileNameToDate(fileName);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream
                (path + File.separator + fileName), "Windows-1252"))) {
            return reader.lines()
                    .map(line -> line.split(";"))
                    .map(values -> MeteorologyData.builder()
                            .date(date)
                            .location(decodeWindows1252ToUTF8(values[0]))
                            .province(decodeWindows1252ToUTF8(values[1]))
                            .maxTemperature(Float.parseFloat(values[2]))
                            .maxTemperatureTime(UtilParsers.parseLocalTime(values[3]))
                            .minTemperature(Float.parseFloat(values[4]))
                            .minTemperatureTime(UtilParsers.parseLocalTime(values[5]))
                            .precipitation(Float.parseFloat(values[6]))
                            .build()
                    )
                    .toList();
        } catch (IOException e) {
            throw new ReadCSVFailException(e.getMessage());
        }
    }

    /**
     * Decodifica un string de windows-1252 a UTF-8
     *
     * @param input String en windows-1252
     * @return String en UTF-8
     */
    private String decodeWindows1252ToUTF8(String input) {
        try {
            return new String(input.getBytes(StandardCharsets.UTF_8), "windows-1252");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return input;
        }
    }
}
