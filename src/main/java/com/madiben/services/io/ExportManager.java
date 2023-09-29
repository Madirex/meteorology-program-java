package com.madiben.services.io;

import com.madiben.exceptions.CreateFolderException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
}
