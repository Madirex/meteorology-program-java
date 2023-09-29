package com.madiben.controllers;

import com.madiben.exceptions.MeteorologyDataException;
import com.madiben.exceptions.MeteorologyDataNotFoundException;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Controlador base
 *
 * @param <T> Entity
 */
public interface BaseController<T> {
    List<T> findAll() throws SQLException;

    Optional<T> findById(String id) throws SQLException, MeteorologyDataNotFoundException;

    Optional<T> save(T entity) throws SQLException;

    Optional<T> update(String id, T entity) throws SQLException, MeteorologyDataException;

    boolean delete(String id) throws SQLException, MeteorologyDataNotFoundException;
}
