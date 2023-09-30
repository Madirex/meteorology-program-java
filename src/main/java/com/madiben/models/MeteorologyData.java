package com.madiben.models;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

/**
 * MeteorologyData
 */
@Data
@Builder
@Getter
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

    public UUID getUuid() {
        return uuid;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public String getProvince() {
        return province;
    }

    public float getMaxTemperature() {
        return maxTemperature;
    }

    public LocalTime getMaxTemperatureTime() {
        return maxTemperatureTime;
    }

    public float getMinTemperature() {
        return minTemperature;
    }

    public LocalTime getMinTemperatureTime() {
        return minTemperatureTime;
    }

    public float getPrecipitation() {
        return precipitation;
    }
}