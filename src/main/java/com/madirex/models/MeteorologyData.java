package com.madirex.models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;

@Data
@Builder
public class MeteorologyData {
    private String locality;
    private String province;
    private float maxTemperature;
    private LocalTime maxTemperatureTime;
    private float minTemperature;
    private LocalTime minTemperatureTime;
    private float precipitation;
}