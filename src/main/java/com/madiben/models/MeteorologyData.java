package com.madiben.models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

/**
 * MeteorologyData
 */
@Data
@Builder
public class MeteorologyData {
    @Builder.Default
    private UUID uuid = UUID.randomUUID();
    private LocalDate date;
    private String location;
    private String province;
    private float maxTemperature;
    private LocalTime maxTemperatureTime;
    private float minTemperature;
    private LocalTime minTemperatureTime;
    private float precipitation;
}