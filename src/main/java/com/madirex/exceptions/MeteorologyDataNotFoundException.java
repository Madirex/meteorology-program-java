package com.madirex.exceptions;

public class MeteorologyDataNotFoundException extends MeteorologyDataException {
    public MeteorologyDataNotFoundException(String message) {
        super("MeteorologyData no encontrado: " + message);
    }
}
