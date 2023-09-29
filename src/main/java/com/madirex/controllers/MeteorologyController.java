package com.madirex.controllers;

import com.madirex.models.MeteorologyData;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.List;

public class MeteorologyController implements BaseController<MeteorologyData>{
    private static MeteorologyController Instance;
    private MeteorologyController() {
    }
    public static MeteorologyController getInstance() {
        if (Instance == null) {
            Instance = new MeteorologyController();
        }
        return Instance;
    }
    List<MeteorologyData> meteorologyDataList;
    private void loadData() {
         String dataPath = "data" + File.separator + "Aemet2017029.csv";
         String appPath = System.getProperty("user.dir");
         Path filePath = Paths.get(appPath + File.separator + dataPath);
        System.out.println("Loading data from: " + filePath);
        // Existe usando Paths
        if (Files.exists(filePath)) {
            System.out.println("File data exists");
        } else {
            System.out.println("File data does not exist");
        }

        try {
            meteorologyDataList = Files.lines(filePath)
                    .skip(1)
                    .map(this::getMeteorology)
                    .toList();
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    private MeteorologyData getMeteorology(String linea) {
        String[] campos = linea.split(";");
        return MeteorologyData.builder()
                .location(campos[0])
                .province(campos[1])
                .maxTemperature(Float.parseFloat(campos[2]))
                .maxTemperatureTime(LocalTime.parse(campos[3]))
                .minTemperature(Float.parseFloat(campos[4]))
                .minTemperatureTime(campos[5])
                .precipitation(Float.parseFloat(campos[6]))
                .build();
    }



}
