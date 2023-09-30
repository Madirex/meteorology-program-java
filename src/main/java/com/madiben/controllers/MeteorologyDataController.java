package com.madiben.controllers;

import com.madiben.exceptions.MeteorologyDataException;
import com.madiben.models.MeteorologyData;
import com.madiben.models.dto.MeteorologyDataGroupedDTO;
import com.madiben.models.dto.MeteorologyDayData;
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
                            .maxTemperature(getDataOfMaxTemperature(dataList))
                            .minTemperature(getDataOfMinTemperature(dataList))
                            .avgMaxTemperature(avgMaxTemperature(dataList))
                            .avgMinTemperature(avgMinTemperature(dataList))
                            .maxPrecipitation(getDataOfMaxPrecipitation(dataList))
                            .avgPrecipitation(avgPrecipitation(dataList))
                            .build();
                }).toList();
    }

    /**
     * Devuelve una lista de MeteorologyDayData con los datos de cada día
     *
     * @return Lista de MeteorologyDayData con los datos de cada día
     */
    public List<MeteorologyDayData> getDayData(){
        List<MeteorologyDayData> list = new ArrayList<>();
        findAll().stream()
                .collect(Collectors.groupingBy(MeteorologyData::getDate))
                .forEach((key, value) -> list.add(MeteorologyDayData.builder()
                .date(key)
                .maxTemperature(getDataOfMaxTemperature(value))
                .minTemperature(getDataOfMinTemperature(value))
                .maxPrecipitation(getDataOfMaxPrecipitation(value)).build()));
        return list;
    }

    /**
     * Devuelve un mapa con los datos de meteorología agrupados por fecha
     *
     * @param province Provincia a consultar
     * @return Mapa con los datos de meteorología agrupados por fecha
     */
    private Map<LocalDate, List<MeteorologyData>> mapDataByDate(String province) {
        return getDataFilterByProvince(province).stream()
                .collect(Collectors.groupingBy(MeteorologyData::getDate));
    }

    /**
     * Devuelve una lista de MeteorologyData con los datos de meteorología de una provincia
     *
     * @param province Provincia a consultar
     * @return Lista de MeteorologyData con los datos de meteorología de una provincia
     */
    private List<MeteorologyData> getDataFilterByProvince(String province) {
        return findAll().stream()
                .filter(data -> data.getProvince().equalsIgnoreCase(province)).toList();
    }

    /**
     * Devuelve los datos de meteorología con mayor temperatura
     *
     * @return Optional de MeteorologyData con los datos de meteorología con mayor temperatura
     */
    private Optional<MeteorologyData> getDataOfMaxTemperature(List<MeteorologyData> dataList) {
        return dataList.stream()
                .max(Comparator.comparingDouble(MeteorologyData::getMaxTemperature));
    }

    /**
     * Devuelve los datos de meteorología con menor temperatura
     *
     * @return Optional de MeteorologyData con los datos de meteorología con menor temperatura
     */
    private Optional<MeteorologyData> getDataOfMinTemperature(List<MeteorologyData> dataList) {
        return dataList.stream()
                .min(Comparator.comparingDouble(MeteorologyData::getMaxTemperature));
    }

    /**
     * Devuelve los datos de meteorología con mayor precipitación
     *
     * @return Optional de MeteorologyData con los datos de meteorología con mayor precipitación
     */
    private Optional<MeteorologyData> getDataOfMaxPrecipitation(List<MeteorologyData> dataList) {
        return dataList.stream()
                .max(Comparator.comparingDouble(MeteorologyData::getPrecipitation));
    }

    /**
     * Devuelve la temperatura mínima de una lista de MeteorologyData
     *
     * @param dataList Lista de MeteorologyData
     * @return Temperatura mínima
     */
    private double avgMinTemperature(List<MeteorologyData> dataList) {
        return dataList.stream()
                .mapToDouble(MeteorologyData::getMinTemperature)
                .average()
                .orElse(0.0);
    }

    /**
     * Devuelve la temperatura máxima de una lista de MeteorologyData
     *
     * @param dataList Lista de MeteorologyData
     * @return Temperatura máxima
     */
    private double avgMaxTemperature(List<MeteorologyData> dataList) {
        return dataList.stream()
                .mapToDouble(MeteorologyData::getMaxTemperature)
                .average()
                .orElse(0.0);
    }

    /**
     * Devuelve una lista de MeteorologyDataGroupedDTO con los datos de meteorología agrupados por fecha y provincia
     *
     * @return Lista de MeteorologyDataGroupedDTO con los datos de meteorología agrupados por fecha y provincia
     */
    public List<MeteorologyDataGroupedDTO> dataGrouper() {
        Map<LocalDate, Map<String, List<MeteorologyData>>> groupedData = findAll().stream()
                .collect(Collectors.groupingBy(MeteorologyData::getDate,
                        Collectors.groupingBy(MeteorologyData::getProvince)));
        List<MeteorologyDataGroupedDTO> result = new ArrayList<>();
        groupedData.forEach((date, provinceDataMap) -> provinceDataMap.forEach((province, meteoList) ->
                result.add(MeteorologyDataGroupedDTO.builder()
                        .date(date)
                        .province(province)
                        .meteorologyData(meteoList)
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

    /**
     * Devuelve la temperatura mínima de una lista de MeteorologyData
     *
     * @param dataList Lista de MeteorologyData
     * @return Temperatura mínima
     */
    public float minTemperature(List<MeteorologyData> dataList) {
        return dataList.stream()
                .min(Comparator.comparing(MeteorologyData::getMinTemperature))
                .map(MeteorologyData::getMinTemperature)
                .orElse(0.0f);
    }

    /**
     * Devuelve la temperatura media de una lista de MeteorologyData
     *
     * @param dataList Lista de MeteorologyData
     * @return Temperatura media
     */
    public double avgPrecipitation(List<MeteorologyData> dataList) {
        return dataList.stream()
                .mapToDouble(MeteorologyData::getPrecipitation)
                .average()
                .orElse(0.0);
    }

    /**
     * Devuelve la temperatura media de una lista de MeteorologyData
     *
     * @param dataList Lista de MeteorologyData
     * @return Temperatura media
     */
    public double avgTemperature(List<MeteorologyData> dataList) {
        return dataList.stream()
                .mapToDouble(e -> ((e.getMaxTemperature() + e.getMinTemperature()) / 2))
                .average()
                .orElse(0.0);
    }

    /**
     * Devuelve una lista de Strings de localidad con precipitación
     *
     * @param dataList Lista de datos de meteorología
     * @return Lista de Strings de localidad con precipitación
     */
    public List<String> locationListWithPrecipitation(List<MeteorologyData> dataList) {
        return dataList.stream()
                .filter(meteorologyData -> meteorologyData.getPrecipitation() > 0)
                .map(MeteorologyData::getLocation)
                .toList();
    }
}