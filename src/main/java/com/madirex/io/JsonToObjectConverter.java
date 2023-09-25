package com.madirex.io;

public abstract class JsonToObjectConverter<I> {
    public abstract I jsonToObject(String jsonPath);
}
