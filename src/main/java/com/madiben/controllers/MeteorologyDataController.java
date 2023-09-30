package com.madiben.controllers;

import com.madiben.exceptions.MeteorologyDataException;
import com.madiben.models.MeteorologyData;
import com.madiben.services.crud.meteorology.MeteorologyDataService;
import com.madiben.services.database.DatabaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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

    public static double tempMax(List<MeteorologyData> dataList) {
        Optional<MeteorologyData> maxTemperatureData = dataList.stream()
                .max(Comparator.comparing(MeteorologyData::getMaxTemperature));


    }

    public static double tempMin(List<MeteorologyData> dataList) {
            Optional<MeteorologyData>  maxTemperature= dataList.stream()
                .min(Comparator.comparing(MeteorologyData::getMinTemperature));
    }
}