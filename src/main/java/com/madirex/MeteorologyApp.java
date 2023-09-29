package com.madirex;

import com.madirex.controllers.MeteorologyDataController;
import com.madirex.exceptions.ReadCSVFailException;
import com.madirex.models.MeteorologyData;
import com.madirex.repositories.meteorology.MeteorologyDataRepositoryImpl;
import com.madirex.services.crud.meteorology.MeteorologyDataServiceImpl;
import com.madirex.services.database.DatabaseManager;
import com.madirex.services.io.CsvManager;

import java.io.File;
import java.sql.SQLException;

public class MeteorologyApp {

    private static MeteorologyApp meteorologyAppInstance;

    private MeteorologyApp() {
    }

    public static MeteorologyApp getInstance() {
        if (meteorologyAppInstance == null) {
            meteorologyAppInstance = new MeteorologyApp();
        }
        return meteorologyAppInstance;
    }

    public void run(){
        MeteorologyDataController controller = new MeteorologyDataController
                (new MeteorologyDataServiceImpl(new MeteorologyDataRepositoryImpl(DatabaseManager.getInstance())));
        try {
            for (MeteorologyData meteorologyData : CsvManager.getInstance()
                    .folderDataToMeteorologyList(System.getProperty("user.dir") + File.separator + "data")) {
                controller.save(meteorologyData);
            }
        } catch (ReadCSVFailException | SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            controller.findAll().forEach(System.out::println);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
