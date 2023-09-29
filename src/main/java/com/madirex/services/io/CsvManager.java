package com.madirex.services.io;

import com.madirex.exceptions.CreateFolderException;
import com.madirex.exceptions.ReadCSVFailException;
import com.madirex.models.MeteorologyData;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
     * Lee un archivo CSV y lo convierte en un Optional de la lista de MeteorologyData
     *
     * @param path Ruta del archivo CSV
     * @return Optional de la lista de MeteorologyData
     */
    public Optional<List<MeteorologyData>> fileToMeteorologyDataList(String path) throws ReadCSVFailException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            return Optional.of(reader.lines()
                    .map(line -> line.split(","))
                    .skip(1)
                    .map(values -> MeteorologyData.builder()
                            //TODO: VALUES LOAD
                            .build()
                    )
                    .toList());
        } catch (IOException e) {
            throw new ReadCSVFailException(e.getMessage());
        }
    }
}
