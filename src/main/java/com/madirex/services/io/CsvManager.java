package com.madirex.services.io;

import com.madirex.exceptions.ReadCSVFailException;
import com.madirex.models.MeteorologyData;
import com.madirex.utils.UtilParsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
            e.printStackTrace();
        }
        return totalData;
    }

    public List<MeteorologyData> fileToMeteorologyDataList(String path, String fileName) throws ReadCSVFailException {
        LocalDate date = UtilParsers.getInstance().parseFileNameToDate(fileName);
        try (BufferedReader reader = new BufferedReader(new FileReader(path + File.separator + fileName))) {
            return reader.lines()
                    .map(line -> line.split(";"))
                    .map(values -> MeteorologyData.builder()
                            .date(date)
                            .location(values[0])
                            .province(values[1])
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
}
