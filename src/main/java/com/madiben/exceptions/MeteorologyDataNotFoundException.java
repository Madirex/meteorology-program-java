package com.madiben.exceptions;

/**
 * MeteorologyDataNotFoundException
 */
public class MeteorologyDataNotFoundException extends MeteorologyDataException {
    public MeteorologyDataNotFoundException(String message) {
        super("MeteorologyData no encontrado: " + message);
    }
}
