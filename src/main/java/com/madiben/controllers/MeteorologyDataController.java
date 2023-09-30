package com.madiben.controllers;

import com.madiben.exceptions.MeteorologyDataException;
import com.madiben.models.MeteorologyData;
import com.madiben.models.dto.MeteorologyDataGropuedDTO;
import com.madiben.models.dto.MeteorologyProvinceDayData;
import com.madiben.services.crud.meteorology.MeteorologyDataService;
import com.madiben.services.database.DatabaseManager;
import com.madiben.services.io.ExportManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.time.LocalDate;
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
    public List<MeteorologyProvinceDayData> getProvinceData(String province) {
        List<MeteorologyData> provinceList = findAll().stream()
                .filter(data -> data.getProvince().equalsIgnoreCase(province)).toList();
        Map<LocalDate, List<MeteorologyData>> dateMap = provinceList.stream()
                .collect(Collectors.groupingBy(MeteorologyData::getDate));
        return dateMap.entrySet().stream().map(entry -> {
                    LocalDate date = entry.getKey();
                    List<MeteorologyData> dataList = entry.getValue();
                    Optional<MeteorologyData> maxTemperatureData = dataList.stream()
                            .max(Comparator.comparingDouble(MeteorologyData::getMaxTemperature));
                    Optional<MeteorologyData> minTemperatureData = dataList.stream()
                            .min(Comparator.comparingDouble(MeteorologyData::getMaxTemperature));
                    double avgMaxTemperature = dataList.stream()
                            .mapToDouble(MeteorologyData::getMaxTemperature)
                            .average()
                            .orElse(0.0);

                    double avgMinTemperature = dataList.stream()
                            .mapToDouble(MeteorologyData::getMinTemperature)
                            .average()
                            .orElse(0.0);
                    Optional<MeteorologyData> maxPrecipitation = dataList.stream()
                            .max(Comparator.comparingDouble(MeteorologyData::getPrecipitation));
                    double avgPrecipitation = dataList.stream()
                            .mapToDouble(MeteorologyData::getPrecipitation)
                            .average()
                            .orElse(0.0);

                    return MeteorologyProvinceDayData.builder()
                            .date(date)
                            .maxTemperature(maxTemperatureData)
                            .minTemperature(minTemperatureData)
                            .avgMaxTemperature(avgMaxTemperature)
                            .avgMinTemperature(avgMinTemperature)
                            .maxPrecipitation(maxPrecipitation)
                            .avgPrecipitation(avgPrecipitation)
                            .build();
                })
                .collect(Collectors.toList());
    }

    public List<MeteorologyDataGropuedDTO> dataGrouper() {
        List<MeteorologyDataGropuedDTO> DataGrouped = findAll().stream()
                .collect(Collectors.groupingBy(MeteorologyData::getProvince))
                .map(entry1 -> {
                    String province1 = entry1.getKey();
                    List<MeteorologyData> dataList1 = entry1.getValue();
                    Optional<MeteorologyData> minTemperatureData1 = dataList1.stream()
                            .min(Comparator.comparingDouble(MeteorologyData::getMinTemperature));
                    return MeteorologyDataGropuedDTO.builder()
                            .date(date)
                            .province(province1)
                            .meteorologyData(minTemperatureData1)
                            .build();
                })
                .toList();
    }

}