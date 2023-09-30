package com.madiben.models.dto;

import com.madiben.models.MeteorologyData;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Clase MeteorologyProvinceData que representa los datos de meteorología de una provincia
 */
@Data
@Builder
public class MeteorologyProvinceDayData {
    private LocalDate date;
    private Optional<MeteorologyData> maxTemperature;
    private Optional<MeteorologyData> minTemperature;
    private double avgMaxTemperature;
    private double avgMinTemperature;
    private Optional<MeteorologyData> maxPrecipitation;
    private double avgPrecipitation;
}