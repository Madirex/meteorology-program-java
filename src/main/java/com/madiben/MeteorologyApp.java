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
                    System.out.println("=DDDDDDDDDDD"); //TODO: FIX
                }
            }
        } catch (ReadCSVFailException e) {
            logger.error("Error al leer el CSV", e);
        }

        //TODO: PRINTEAR CONSULTAS API STREAM
        System.out.println("Número de elementos: " + controller.findAll().size());

        /////////////////////////////////////
        exportProvinceToJson("Madrid");
        finish();
    }

    /**
     * Método que exporta los datos de una provincia a un fichero JSON
     *
     * @param province Provincia a exportar
     */
    private void exportProvinceToJson(String province) {
        String msg = "Exportando datos de la provincia " + province + " a JSON...";
        logger.info(msg);
        if (controller.exportDataByProvince(province)) {
            msg = "Datos de la provincia " + province + " exportados a JSON";
            logger.info(msg);
        } else {
            msg = "Error al exportar los datos de la provincia \" + province + \" a JSON";
            logger.error(msg);
        }
    }

    /**
     * Método de finalización del programa
     */
    private void finish() {
        DatabaseManager.getInstance().close();
    }
}
