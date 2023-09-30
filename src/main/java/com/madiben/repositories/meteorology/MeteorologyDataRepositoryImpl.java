package com.madiben.repositories.meteorology;

import com.madiben.models.MeteorologyData;
import com.madiben.services.database.DatabaseManager;
import lombok.RequiredArgsConstructor;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementación de la interfaz MeteorologyDataRepository
 */
@RequiredArgsConstructor
public class MeteorologyDataRepositoryImpl implements MeteorologyDataRepository {
    private final DatabaseManager database;

    /**
     * Devuelve todos los elementos del repositorio
     *
     * @return Optional de la lista de elementos
     */
    @Override
    public List<MeteorologyData> findAll() throws SQLException {
        List<MeteorologyData> list = new ArrayList<>();
        var sql = "SELECT * FROM meteorologyData";
        database.beginTransaction();
        var res = database.select(sql).orElseThrow();
        while (res.next()) {
            list.add(MeteorologyData.builder()
                    .uuid(UUID.fromString(res.getString("ID")))
                    .date(res.getDate("DayDate").toLocalDate())
                    .location(res.getString("Location"))
                    .province(res.getString("Province"))
                    .maxTemperature(res.getFloat("MaxTemperature"))
                    .maxTemperatureTime(res.getTime("MaxTemperatureTime").toLocalTime())
                    .minTemperature(res.getFloat("MinTemperature"))
                    .minTemperatureTime(res.getTime("MinTemperatureTime").toLocalTime())
                    .precipitation(res.getFloat("Precipitation"))
                    .build());
        }
        database.commit();
        return list;
    }

    /**
     * Busca un elemento en el repositorio por su id
     *
     * @param id Id del elemento a buscar
     * @return Optional del elemento encontrado
     */
    @Override
    public Optional<MeteorologyData> findById(String id) throws SQLException {
        Optional<MeteorologyData> optReturn = Optional.empty();
        var sql = "SELECT * FROM meteorologyData WHERE cod = ?";
        database.beginTransaction();
        var res = database.select(sql, id).orElseThrow();
        if (res.next()) {
            optReturn = Optional.of(MeteorologyData.builder()
                    .uuid(UUID.fromString(res.getString("ID")))
                    .date(res.getDate("DayDate").toLocalDate())
                    .location(res.getString("Location"))
                    .province(res.getString("Province"))
                    .maxTemperature(res.getFloat("MaxTemperature"))
                    .maxTemperatureTime(res.getTime("MaxTemperatureTime").toLocalTime())
                    .minTemperature(res.getFloat("MinTemperature"))
                    .minTemperatureTime(res.getTime("MinTemperatureTime").toLocalTime())
                    .precipitation(res.getFloat("Precipitation"))
                    .build());
        }
        database.commit();
        return optReturn;
    }

    /**
     * Guarda un elemento en el repositorio
     *
     * @param entity Elemento a guardar
     * @return Optional del elemento guardado
     */
    @Override
    public Optional<MeteorologyData> save(MeteorologyData entity) throws SQLException {
        var sql = "INSERT INTO meteorologyData (ID, DayDate, Location, Province, MaxTemperature, MaxTemperatureTime, " +
                "MinTemperature, MinTemperatureTime, Precipitation) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        database.beginTransaction();
        database.insertAndGetKey(sql, entity.getUuid().toString(),
                entity.getDate(),
                entity.getLocation(),
                entity.getProvince(),
                entity.getMaxTemperature(),
                entity.getMaxTemperatureTime(),
                entity.getMinTemperature(),
                entity.getMinTemperatureTime(),
                entity.getPrecipitation());
        database.commit();
        return Optional.of(entity);
    }

    /**
     * Borra un elemento del repositorio
     *
     * @param id Id del elemento a borrar
     * @return ¿Borrado?
     */
    @Override
    public boolean delete(String id) throws SQLException {
        var sql = "DELETE FROM meteorologyData WHERE cod= ?";
        database.beginTransaction();
        var rs = database.delete(sql, id);
        database.commit();
        return (rs == 1);
    }

    /**
     * Actualiza un elemento del repositorio
     *
     * @param id     Id del elemento a actualizar
     * @param entity Elemento con los nuevos datos
     * @return Optional del elemento actualizado
     */
    @Override
    public Optional<MeteorologyData> update(String id, MeteorologyData entity) throws SQLException {
        var sql = "UPDATE meteorologyData SET DayDate = ?, Location = ?, Province = ?, MaxTemperature = ?, " +
                "MaxTemperatureTime = ?, " +
                "MinTemperature = ?, MinTemperatureTime = ?, Precipitation = ? WHERE ID = ?";
        database.beginTransaction();
        database.update(sql,
                entity.getDate(),
                entity.getLocation(),
                entity.getProvince(),
                entity.getMaxTemperature(),
                entity.getMaxTemperatureTime(),
                entity.getMinTemperature(),
                entity.getMinTemperatureTime(),
                entity.getPrecipitation(),
                id);
        database.commit();
        return Optional.of(entity);
    }
}

