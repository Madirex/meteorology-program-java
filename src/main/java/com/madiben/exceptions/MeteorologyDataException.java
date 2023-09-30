package com.madiben.exceptions;

/**
 * MeteorologyDataException
 */
public abstract class MeteorologyDataException extends Exception {
    protected MeteorologyDataException(String message) {
        super(message);
    }
}