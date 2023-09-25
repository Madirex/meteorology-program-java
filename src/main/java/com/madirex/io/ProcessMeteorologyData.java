package com.madirex.io;

import com.madirex.models.MeteorologyData;

import java.io.File;
import java.nio.file.Files;

public class ProcessMeteorologyData extends JsonToObjectConverter {
    public static final String PATH_FILES = System.getProperty("user.dir") + File.separator + "src" + File.separator +
            "main" + File.separator + "resources" + File.separator + "data";

    @Override
    public MeteorologyData jsonToObject(String jsonPath) {
        //TODO: DO
        //Files.lines(jsonPath, Charset.forName("windows-1252"))
        return null;
    }
}
