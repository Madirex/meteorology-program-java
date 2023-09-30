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
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

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
        readCSVFilesAtFolderAndSaveToDatabase();
        printQueries();
        exportProvinceToJson("Madrid");
        finish();
    }

    /**
     * Método que lee los archivos CSV de la carpeta data y los guarda en la base de datos
     */
    private void readCSVFilesAtFolderAndSaveToDatabase() {
        try {
            for (MeteorologyData meteorologyData : CsvManager.getInstance()
                    .folderDataToMeteorologyList(System.getProperty("user.dir") + File.separator + "data")) {
                controller.save(meteorologyData);
            }
        } catch (ReadCSVFailException e) {
            logger.error("Error al leer el CSV", e);
        }
    }

    /**
     * Método que imprime los datos de las consultas
     */
    private void printQueries() {
        printDataDays();
        printDataGroupedByProvincesAndDate();
        printMaxPrecipitationData();
        printDayDataByProvince("Barcelona");
    }

    /**
     * Método que imprime los datos de todos los días
     */
    private void printDataDays() {
        StringBuilder str = new StringBuilder();
        controller.getDayData().forEach(d -> {
            String maxTempLocation = "-";
            String minTempLocation = "-";
            String maxPrecipitationLocation = "-";
            float maxPrecipitation = 0.0f;
            if (d.getMaxTemperature().isPresent()) {
                maxTempLocation = d.getMaxTemperature().get().getLocation();
            }
            if (d.getMinTemperature().isPresent()) {
                minTempLocation = d.getMinTemperature().get().getLocation();
            }
            if (d.getMaxPrecipitation().isPresent()) {
                maxPrecipitationLocation = d.getMaxPrecipitation().get().getLocation();
                maxPrecipitation = d.getMaxPrecipitation().get().getPrecipitation();
            }
            str.append("\n");
            str.append("Fecha: ").append(d.getDate()).append("\n")
                    .append("\t").append("- Lugar temperatura máxima: ").append(maxTempLocation).append("\n")
                    .append("\t").append("- Lugar temperatura mínima: ").append(minTempLocation).append("\n").append("\n")
                    .append("\t").append("- Lugar precipitación máxima: ").append(maxPrecipitationLocation).append("\n")
                    .append("\t").append("- Precipitación máxima: ").append(maxPrecipitation).append("\n")
                    .append("\n");
        });
        if (str.isEmpty()) {
            str.append("No hay datos\n");
        }
        logger.info(str.toString());
    }

    /**
     * Método que imprime los datos agrupados por fecha y provincia
     */
    private void printDataGroupedByProvincesAndDate() {
        StringBuilder str = new StringBuilder();
        controller.dataGrouper().forEach(e -> {
            str.append("\n");
            str.append("Fecha: ").append(e.getDate()).append("\t").append("Provincia: ").append(e.getProvince())
                    .append("\n");
            str.append("Máxima temperatura: ").append(controller.maxTemperature(e.getMeteorologyData())).append("\n");
            str.append("Mínima temperatura: ").append(controller.minTemperature(e.getMeteorologyData())).append("\n");
            str.append("Media de temperatura: ").append(String.format("%.2f", controller
                    .avgTemperature(e.getMeteorologyData()))).append("\n");
            str.append("Media de precipitación: ").append(String.format("%.2f", controller
                    .avgPrecipitation(e.getMeteorologyData()))).append("\n");
            StringBuilder locations = new StringBuilder();
            controller.locationListWithPrecipitation(e.getMeteorologyData()).forEach(d ->
                    locations.append("- ").append(d).append("\n"));
            if (locations.isEmpty()) {
                str.append("No ha llovido en ningún lugar\n");
            } else {
                str.append("Lugares donde ha llovido: \n").append(locations);
            }
        });
        if (str.isEmpty()) {
            str.append("No hay datos\n");
        }
        logger.info(str.toString());
    }

    /**
     * Método que imprime los datos de la localidad donde más ha llovido
     */
    private void printMaxPrecipitationData() {
        Optional<MeteorologyData> data = controller.getMaxPrecipitationData();
        if (data.isPresent()) {
            String strData = "\n\nLugar donde más ha llovido: " + data.get().getLocation() + "\n\n";
            logger.info(strData);
        } else {
            logger.info("\n\nNo hay datos de precipitación\n\n");
        }
    }

    /**
     * Método que imprime datos de todos los días de una provincia
     *
     * @param province Provincia a imprimir
     */
    private void printDayDataByProvince(String province) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append("Datos de la provincia ").append(province).append(": ").append("\n");
        controller.getProvinceDataFilterByProvince(province).forEach(e -> {

            AtomicReference<Float> maxTemperature = new AtomicReference<>(0.0f);
            AtomicReference<String> maxTemperatureLocation = new AtomicReference<>("");
            AtomicReference<Float> minTemperature = new AtomicReference<>(0.0f);
            AtomicReference<String> minTemperatureLocation = new AtomicReference<>("");
            AtomicReference<Float> maxPrecipitation = new AtomicReference<>(0.0f);
            AtomicReference<String> maxPrecipitationLocation = new AtomicReference<>("");

            e.getMaxTemperature().ifPresent(meteorologyData -> {
                maxTemperature.set(meteorologyData.getMaxTemperature());
                maxTemperatureLocation.set(meteorologyData.getLocation());
            });

            e.getMinTemperature().ifPresent(meteorologyData -> {
                minTemperature.set(meteorologyData.getMinTemperature());
                minTemperatureLocation.set(meteorologyData.getLocation());
            });

            e.getMaxPrecipitation().ifPresent(meteorologyData -> {
                maxPrecipitation.set(meteorologyData.getPrecipitation());
                maxPrecipitationLocation.set(meteorologyData.getLocation());
            });

            sb.append("\n").append("Día: ").append(e.getDate()).append("\n");
            sb.append("Temperatura máxima: ").append(maxTemperature).append("\n");
            sb.append("\tLugar: ").append(maxTemperatureLocation).append("\n");
            sb.append("Temperatura mínima: ").append(minTemperature).append("\n");
            sb.append("\tLugar: ").append(minTemperatureLocation).append("\n");
            sb.append("Media de la temperatura máxima: ").append(String.format("%.2f", e.getAvgMaxTemperature())).append("\n");
            sb.append("Media de la temperatura mínima: ").append(String.format("%.2f", e.getAvgMinTemperature())).append("\n");
            sb.append("Precipitación máxima: ").append(maxPrecipitation).append("\n");
            sb.append("\tLugar: ").append(maxPrecipitationLocation).append("\n");
            sb.append("Precipitación media: ").append(String.format("%.2f", e.getAvgPrecipitation())).append("\n");
            sb.append("\n\n");
        });
        logger.info(sb.toString());
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
