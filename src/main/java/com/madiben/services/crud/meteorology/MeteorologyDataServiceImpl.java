package com.madiben.services.crud.meteorology;

import com.madiben.models.MeteorologyData;
import com.madiben.repositories.meteorology.MeteorologyDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Implementación de MeteorologyDataService
 */
public class MeteorologyDataServiceImpl implements MeteorologyDataService {
    private static MeteorologyDataServiceImpl instance;
    private final Logger logger = LoggerFactory.getLogger(MeteorologyDataServiceImpl.class);
    private final MeteorologyDataRepository meteorologyDataRepository;

    /**
     * Constructor de MeteorologyDataServiceImpl
     *
     * @param meteorologyDataRepository Repositorio de MeteorologyData
     */
    private MeteorologyDataServiceImpl(MeteorologyDataRepository meteorologyDataRepository) {
        this.meteorologyDataRepository = meteorologyDataRepository;
    }

    /**
     * SINGLETON
     * Devuelve la instancia de MeteorologyDataServiceImpl
     *
     * @param meteorologyDataRepository Repositorio de MeteorologyData
     * @return Instancia de MeteorologyDataServiceImpl
     */
    public static MeteorologyDataServiceImpl getInstance(MeteorologyDataRepository meteorologyDataRepository) {
        if (instance == null) {
            instance = new MeteorologyDataServiceImpl(meteorologyDataRepository);
        }
        return instance;
    }

    /**
     * Devuelve todos los elementos del repositorio
     *
     * @return Optional de la lista de elementos
     */
    @Override
    public List<MeteorologyData> findAll() throws SQLException {
        logger.debug("Obteniendo todos los meteorologyDatas");
        return meteorologyDataRepository.findAll();
    }

    /**
     * Busca un elemento en el repositorio por su id
     *
     * @param id Id del elemento a buscar
     * @return Optional del elemento encontrado
     */
    @Override
    public Optional<MeteorologyData> findById(String id) throws SQLException {
        logger.debug("Obteniendo meteorologyData por id");
        logger.debug("MeteorologyData no encontrado en caché, buscando en base de datos");
        return meteorologyDataRepository.findById(id);
    }

    /**
     * Guarda un elemento en el repositorio
     *
     * @param meteorologyData Elemento a guardar
     * @return Optional del elemento guardado
     */
    @Override
    public Optional<MeteorologyData> save(MeteorologyData meteorologyData) throws SQLException {
        Optional<MeteorologyData> modified;
        logger.debug("Guardando meteorologyData");
        modified = meteorologyDataRepository.save(meteorologyData);
        return modified;
    }

    /**
     * Actualiza un elemento del repositorio
     *
     * @param meteorologyDataId  Id del elemento a actualizar
     * @param newMeteorologyData Elemento con los nuevos datos
     * @return Optional del elemento actualizado
     */
    @Override
    public Optional<MeteorologyData> update(String meteorologyDataId, MeteorologyData newMeteorologyData) throws SQLException {
        Optional<MeteorologyData> modified;
        logger.debug("Actualizando meteorologyData");
        modified = meteorologyDataRepository.update(meteorologyDataId, newMeteorologyData);
        return modified;
    }

    /**
     * Borra un elemento del repositorio
     *
     * @param id Id del elemento a borrar
     * @return ¿Borrado?
     */
    @Override
    public boolean delete(String id) throws SQLException {
        boolean removed;
        logger.debug("Eliminando meteorologyData");
        removed = meteorologyDataRepository.delete(id);
        return removed;
    }
}