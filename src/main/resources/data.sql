DROP TABLE IF EXISTS MeteorologyData;
CREATE TABLE IF NOT EXISTS MeteorologyData (
                                 ID UUID DEFAULT RANDOM_UUID() NOT NULL PRIMARY KEY,
                                 Location VARCHAR(255),
                                 Province VARCHAR(255),
                                 MaxTemperature DECIMAL(5, 2),
                                 MaxTemperatureTime TIME,
                                 MinTemperature DECIMAL(5, 2),
                                 MinTemperatureTime TIME,
                                 Precipitation DECIMAL(5, 2)
);