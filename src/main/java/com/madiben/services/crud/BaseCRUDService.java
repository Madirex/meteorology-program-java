package com.madiben.services.crud;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define las operaciones CRUD de BaseCRUDService
 */
public interface BaseCRUDService<I, E extends Throwable> {
    List<I> findAll() throws SQLException;

    Optional<I> findById(String id) throws SQLException;

    Optional<I> save(I item) throws SQLException, E;

    Optional<I> update(String id, I newI) throws SQLException, E;

    boolean delete(String id) throws SQLException, E;
}
