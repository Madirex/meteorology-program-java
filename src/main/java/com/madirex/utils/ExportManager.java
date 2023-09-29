package com.madirex.utils;

import com.madirex.exceptions.CreateFolderException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ExportManager {

    private static ExportManager exportManagerInstance;

    private ExportManager() {
    }

    public static ExportManager getInstance() {
        if (exportManagerInstance == null) {
            exportManagerInstance = new ExportManager();
        }
        return exportManagerInstance;
    }

    /**
     * Crea la carpeta out si no existe
     */
    private void createOutFolderIfNotExists() throws CreateFolderException {
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
