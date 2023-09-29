package com.madirex.controllers;

import com.madirex.exceptions.MeteorologyDataException;
import com.madirex.exceptions.MeteorologyDataNotFoundException;
import com.madirex.models.MeteorologyData;
import com.madirex.services.crud.meteorology.MeteorologyDataService;
import com.madirex.services.database.DatabaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class MeteorologyDataController implements BaseController<MeteorologyData> {
    private final Logger logger = LoggerFactory.getLogger(DatabaseManager.class);

    private final MeteorologyDataService meteorologyDataService;

    public MeteorologyDataController(MeteorologyDataService meteorologyDataService) {
        this.meteorologyDataService = meteorologyDataService;
    }

    public List<MeteorologyData> findAll() throws SQLException {
        logger.debug("FindAll");
        return meteorologyDataService.findAll();
    }

    public Optional<MeteorologyData> findById(String id) throws SQLException {
        String msg = "FindById " + id;
        logger.debug(msg);
        return meteorologyDataService.findById(id);
    }

    public Optional<MeteorologyData> save(MeteorologyData meteorologyData) throws SQLException {
        String msg = "Save " + meteorologyData;
        logger.debug(msg);
        try {
            return meteorologyDataService.save(meteorologyData);
        } catch (MeteorologyDataException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<MeteorologyData> update(String id, MeteorologyData meteorologyData) throws SQLException,
            MeteorologyDataNotFoundException {
        String msg = "Update " + meteorologyData;
        logger.debug(msg);
        try {
            return meteorologyDataService.update(id, meteorologyData);
        } catch (MeteorologyDataException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<MeteorologyData> delete(String id) throws SQLException, MeteorologyDataNotFoundException {
        String msg = "Delete " + id;
        logger.debug(msg);
        var meteorologyData = meteorologyDataService.findById(id).orElseThrow(() ->
                new MeteorologyDataNotFoundException("No se ha encontrado el meteorologyData con id " + id));
        try {
            meteorologyDataService.delete(id);
        } catch (MeteorologyDataException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(meteorologyData);
    }
}