package com.madirex.repositories.meteorology;

import com.madirex.models.MeteorologyData;
import com.madirex.repositories.CRUDRepository;

import java.sql.SQLException;
import java.util.List;

/**
 * Interfaz que define las operaciones CRUD de MeteorologyDataRepository
 */
public interface MeteorologyDataRepository extends CRUDRepository<MeteorologyData, String> {

}
