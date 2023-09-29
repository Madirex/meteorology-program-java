package com.madirex.services.crud.meteorology;

import com.madirex.models.MeteorologyData;
import com.madirex.repositories.meteorology.MeteorologyDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class MeteorologyDataServiceImpl implements MeteorologyDataService {
    private static MeteorologyDataServiceImpl instance;
    private final Logger logger = LoggerFactory.getLogger(MeteorologyDataServiceImpl.class);
    private final MeteorologyDataRepository meteorologyDataRepository;

    public MeteorologyDataServiceImpl(MeteorologyDataRepository meteorologyDataRepository) {
        this.meteorologyDataRepository = meteorologyDataRepository;
    }

    public static MeteorologyDataServiceImpl getInstance(MeteorologyDataRepository meteorologyDataRepository) {
        if (instance == null) {
            instance = new MeteorologyDataServiceImpl(meteorologyDataRepository);
        }
        return instance;
    }

    @Override
    public List<MeteorologyData> findAll() throws SQLException {
        logger.debug("Obteniendo todos los meteorologyDatas");
        return meteorologyDataRepository.findAll();
    }

    @Override
    public Optional<MeteorologyData> findById(String id) throws SQLException {
        logger.debug("Obteniendo meteorologyData por id");
        logger.debug("MeteorologyData no encontrado en caché, buscando en base de datos");
        return meteorologyDataRepository.findById(id);
    }

    @Override
    public Optional<MeteorologyData> save(MeteorologyData meteorologyData) throws SQLException {
        Optional<MeteorologyData> modified;
        logger.debug("Guardando meteorologyData");
        modified = meteorologyDataRepository.save(meteorologyData);
        return modified;
    }

    @Override
    public Optional<MeteorologyData> update(String meteorologyDataId, MeteorologyData newMeteorologyData) throws SQLException {
        Optional<MeteorologyData> modified;
        logger.debug("Actualizando meteorologyData");
        modified = meteorologyDataRepository.update(meteorologyDataId, newMeteorologyData);
        return modified;
    }

    @Override
    public boolean delete(String id) throws SQLException {
        boolean removed;
        logger.debug("Eliminando meteorologyData");
        removed = meteorologyDataRepository.delete(id);
        return removed;
    }
}