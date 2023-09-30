package com.madiben.services.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.madiben.adapters.LocalDateAdapter;
import com.madiben.adapters.LocalTimeAdapter;
import com.madiben.exceptions.CreateFolderException;
import com.madiben.models.MeteorologyData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Clase ExportManager que administra la exportación de datos
 */
public class ExportManager {

    private static ExportManager exportManagerInstance;

    /**
     * Constructor privado para evitar la creación de instancia
     * SINGLETON
     */
    private ExportManager() {
    }

    /**
     * Obtiene la instancia de ExportManager
     * SINGLETON
     *
     * @return Instancia de ExportManager
     */
    public static ExportManager getInstance() {
        if (exportManagerInstance == null) {
            exportManagerInstance = new ExportManager();
        }
        return exportManagerInstance;
    }

    /**
     * Crea la carpeta out si no existe
     */
    public void createOutFolderIfNotExists() throws CreateFolderException {
        try {
            Path folderPath = Paths.get("out");
            if (!Files.exists(folderPath)) {
                Files.createDirectories(folderPath);
            }
        } catch (IOException e) {
            throw new CreateFolderException(e.getMessage());
        }
    }

    /**
     * Exporta los datos de meteorología a un archivo CSV
     *
     * @param data     Lista de MeteorologyData
     * @param fileName Nombre del archivo
     * @throws CreateFolderException Si no se puede crear la carpeta out
     * @throws IOException           Si no se puede crear el archivo
     */
    public void exportMeteorologyData(List<MeteorologyData> data, String fileName) throws CreateFolderException,
            IOException {
        createOutFolderIfNotExists();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                .setPrettyPrinting()
                .create();
        FileWriter writer = null;
        try {
            writer = new FileWriter("out" + File.separator + fileName + ".json");
            gson.toJson(data, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}
