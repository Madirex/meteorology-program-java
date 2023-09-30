package com.madiben;

import com.madiben.controllers.MeteorologyDataController;
import com.madiben.exceptions.ReadCSVFailException;
import com.madiben.models.MeteorologyData;
import com.madiben.repositories.meteorology.MeteorologyDataRepositoryImpl;
import com.madiben.services.crud.meteorology.MeteorologyDataServiceImpl;
import com.madiben.services.database.DatabaseManager;
import com.madiben.services.io.CsvManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Clase principal de la aplicación
 */
public class MeteorologyApp {
    private MeteorologyDataController controller = new MeteorologyDataController
            (MeteorologyDataServiceImpl.getInstance(new MeteorologyDataRepositoryImpl(DatabaseManager.getInstance())));

    private static MeteorologyApp meteorologyAppInstance;
    private final Logger logger = LoggerFactory.getLogger(MeteorologyApp.class);

    /**
     * Constructor privado de MeteorologyApp
     */
    private MeteorologyApp() {
    }

    /**
     * Método que devuelve la instancia de MeteorologyApp
     *
     * @return Instancia de MeteorologyApp
     */
    public static MeteorologyApp getInstance() {
        if (meteorologyAppInstance == null) {
            meteorologyAppInstance = new MeteorologyApp();
        }
        return meteorologyAppInstance;
    }

    /**
     * Método que ejecuta la aplicación
     */
    public void run() {
        try {
            for (MeteorologyData meteorologyData : CsvManager.getInstance()
                    .folderDataToMeteorologyList(System.getProperty("user.dir") + File.separator + "data")) {
                controller.save(meteorologyData);
                if (meteorologyData.getLocation().equalsIgnoreCase("Leciñena")) {
                    System.out.println("=DDDDDDDDDDD");
                }
            }
        } catch (ReadCSVFailException e) {
            logger.error("Error al leer el CSV", e);
        }

        //TODO: PRINTEAR CONSULTAS API STREAM
        System.out.println("Número de elementos: " + controller.findAll().size());

        //TODO: EXPORTAR LOS DATOS DE UNA PROVINCIA DADA A UN FICHERO JSON
        System.out.println("áfs");

        finish();
    }

    private void finish() {
        DatabaseManager.getInstance().close();
    }
}
