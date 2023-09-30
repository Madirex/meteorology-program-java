package com.madiben.controllers;

import com.madiben.exceptions.MeteorologyDataException;
import com.madiben.models.MeteorologyData;
import com.madiben.models.dto.MeteorologyDataGropuedDTO;
import com.madiben.models.dto.MeteorologyProvinceDayData;
import com.madiben.services.crud.meteorology.MeteorologyDataService;
import com.madiben.services.database.DatabaseManager;
import com.madiben.services.io.ExportManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controlador de MeteorologyData
 */
public class MeteorologyDataController implements BaseController<MeteorologyData> {
    private final Logger logger = LoggerFactory.getLogger(DatabaseManager.class);
    List<MeteorologyData> meteorologyDataList;
    private final MeteorologyDataService meteorologyDataService;

    /**
     * Constructor
     *
     * @param meteorologyDataService Servicio de MeteorologyData
     */
    public MeteorologyDataController(MeteorologyDataService meteorologyDataService) {
        this.meteorologyDataService = meteorologyDataService;
    }

    /**
     * Busca todos los elementos en el repositorio
     *
     * @return Lista de elementos encontrados
     */
    public List<MeteorologyData> findAll() {
        logger.debug("FindAll");
        try {
            return meteorologyDataService.findAll();
        } catch (SQLException e) {
            logger.error("Error Find All: ", e);
        }
        return new ArrayList<>();
    }

    /**
     * Busca un elemento en el repositorio por su id
     *
     * @param id Id del elemento a buscar
     * @return Optional del elemento encontrado
     */
    public Optional<MeteorologyData> findById(String id) {
        String msg = "FindById " + id;
        logger.debug(msg);
        try {
            return meteorologyDataService.findById(id);
        } catch (SQLException e) {
            logger.error(msg, e);
        }
        return Optional.empty();
    }

    /**
     * Guarda un elemento en el repositorio
     *
     * @param meteorologyData Elemento a guardar
     * @return Optional del elemento guardado
     */
    public Optional<MeteorologyData> save(MeteorologyData meteorologyData) {
        String msg = "Save " + meteorologyData;
        logger.debug(msg);
        try {
            return meteorologyDataService.save(meteorologyData);
        } catch (MeteorologyDataException | SQLException e) {
            logger.error(msg, e);
        }
        return Optional.empty();
    }

    /**
     * Actualiza un elemento del repositorio
     *
     * @param id Id del elemento a actualizar
     * @return Optional del elemento actualizado
     */
    public Optional<MeteorologyData> update(String id, MeteorologyData meteorologyData) {
        String msg = "Update " + meteorologyData;
        logger.debug(msg);
        try {
            return meteorologyDataService.update(id, meteorologyData);
        } catch (SQLException | MeteorologyDataException e) {
            logger.error(msg, e);
        }
        return Optional.empty();
    }

    /**
     * Elimina un elemento del repositorio
     *
     * @param id Id del elemento a eliminar
     * @return ¿eliminado?
     */
    public boolean delete(String id) {
        boolean removed = false;
        String msg = "Delete " + id;
        logger.debug(msg);
        try {
            meteorologyDataService.findById(id);
            removed = meteorologyDataService.delete(id);
        } catch (SQLException | MeteorologyDataException e) {
            logger.error(msg, e);
        }
        return removed;
    }

    /**
     * Exporta los datos de una provincia a un archivo CSV
     *
     * @param province Provincia a exportar
     * @return ¿Exportado?
     */
    public boolean exportDataByProvince(String province) {
        try {
            ExportManager.getInstance().exportMeteorologyData(findAll().stream().filter(meteorologyData ->
                    meteorologyData.getProvince().equalsIgnoreCase(province)).toList(), province);
        } catch (Exception e) {
            logger.error("Error al exportar los datos por provincia", e);
            return false;
        }
        return true;
    }

    //TODO: JDOCS y FIXES

    public double tempMax(List<MeteorologyData> dataList) {
        Optional<MeteorologyData> maxTemperatureData = dataList.stream()
                .max(Comparator.comparing(MeteorologyData::getMaxTemperature));
        return 0.0;
    }

    public double tempMin(List<MeteorologyData> dataList) {
        Optional<MeteorologyData> maxTemperature = dataList.stream()
                .min(Comparator.comparing(MeteorologyData::getMinTemperature));
        return 0.0;
    }


    /**
     * Devuelve los datos de meteorología con mayor precipitación
     *
     * @return Optional de MeteorologyData con los datos de meteorología con mayor precipitación
     */
    public Optional<MeteorologyData> getMaxPrecipitationData() {
        return findAll().stream()
                .max(Comparator.comparing(MeteorologyData::getPrecipitation));
    }

    /**
     * Devuelve una lista de MeteorologyProvinceDayData con los datos de cada día de la provincia
     *
     * @param province Provincia a consultar
     * @return Lista de MeteorologyProvinceDayData con los datos de cada día de la provincia
     */
    public List<MeteorologyProvinceDayData> getProvinceDataFilterByProvince(String province) {
        Map<LocalDate, List<MeteorologyData>> dateMap = mapDataByDate(province);
        return dateMap.entrySet().stream().map(entry -> {
                    List<MeteorologyData> dataList = entry.getValue();
                    return MeteorologyProvinceDayData.builder()
                            .date(entry.getKey())
                            .maxTemperature(getMaxTemperature(dataList))
                            .minTemperature(getMinTemperature(dataList))
                            .avgMaxTemperature(getAvgMaxTemperature(dataList))
                            .avgMinTemperature(getAvgMinTemperature(dataList))
                            .maxPrecipitation(getMaxPrecipitation(dataList))
                            .avgPrecipitation(getAvgPrecipitation(dataList))
                            .build();
                })
                .collect(Collectors.toList());
    }

    @NotNull
    private Map<LocalDate, List<MeteorologyData>> mapDataByDate(String province) {
        return filterByProvince(province).stream()
                .collect(Collectors.groupingBy(MeteorologyData::getDate));
    }

    @NotNull
    private List<MeteorologyData> filterByProvince(String province) {
        return findAll().stream()
                .filter(data -> data.getProvince().equalsIgnoreCase(province)).toList();
    }

    private static double getAvgPrecipitation(List<MeteorologyData> dataList) {
        return dataList.stream()
                .mapToDouble(MeteorologyData::getPrecipitation)
                .average()
                .orElse(0.0);
    }

    @NotNull
    private static Optional<MeteorologyData> getMaxTemperature(List<MeteorologyData> dataList) {
        return dataList.stream()
                .max(Comparator.comparingDouble(MeteorologyData::getMaxTemperature));
    }

    @NotNull
    private static Optional<MeteorologyData> getMinTemperature(List<MeteorologyData> dataList) {
        return dataList.stream()
                .min(Comparator.comparingDouble(MeteorologyData::getMaxTemperature));
    }

    @NotNull
    private static Optional<MeteorologyData> getMaxPrecipitation(List<MeteorologyData> dataList) {
        return dataList.stream()
                .max(Comparator.comparingDouble(MeteorologyData::getPrecipitation));
    }

    private static double getAvgMinTemperature(List<MeteorologyData> dataList) {
        return dataList.stream()
                .mapToDouble(MeteorologyData::getMinTemperature)
                .average()
                .orElse(0.0);
    }

    private static double getAvgMaxTemperature(List<MeteorologyData> dataList) {
        return dataList.stream()
                .mapToDouble(MeteorologyData::getMaxTemperature)
                .average()
                .orElse(0.0);
    }

    /**
     * Devuelve una lista de MeteorologyDataGropuedDTO con los datos de meteorología agrupados por fecha y provincia
     *
     * @return Lista de MeteorologyDataGropuedDTO con los datos de meteorología agrupados por fecha y provincia
     */
    public List<MeteorologyDataGropuedDTO> dataGrouper() {
        Map<LocalDate, Map<String, List<MeteorologyData>>> groupedData = findAll().stream()
                .collect(Collectors.groupingBy(MeteorologyData::getDate,
                        Collectors.groupingBy(MeteorologyData::getProvince)));
        List<MeteorologyDataGropuedDTO> result = new ArrayList<>();
        groupedData.forEach((date, provinceDataMap) -> provinceDataMap.forEach((province, meteorologyDataList) ->
                result.add(MeteorologyDataGropuedDTO.builder()
                        .date(date)
                        .province(province)
                        .meteorologyData(meteorologyDataList)
                        .build()
                )));
        return result;
    }

    /**
     * Devuelve la temperatura máxima de una lista de MeteorologyData
     *
     * @param dataList Lista de MeteorologyData
     * @return Temperatura máxima
     */
    public float maxTemperature(List<MeteorologyData> dataList) {
        return dataList.stream()
                .max(Comparator.comparing(MeteorologyData::getMaxTemperature))
                .map(MeteorologyData::getMaxTemperature)
                .orElse(0.0f);
    }

    public float minTemperature(List<MeteorologyData> dataList) {
        return dataList.stream()
                .min(Comparator.comparing(MeteorologyData::getMinTemperature))
                .map(MeteorologyData::getMinTemperature)
                .orElse(0.0f);
    }

    public double avgPrecipitation(List<MeteorologyData> dataList) {
        return  dataList.stream()
                .mapToDouble(meteorologyData -> meteorologyData.getPrecipitation())
                .average()
                .orElse(0.0);



    }
    public List<String> withPrecipitation(List<MeteorologyData> dataList) {
        return dataList.stream()
                .filter(meteorologyData -> meteorologyData.getPrecipitation() > 0)
                .map(MeteorologyData::getLocation)
                .toList();
    }



}
