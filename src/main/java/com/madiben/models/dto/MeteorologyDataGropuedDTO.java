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
public class MeteorologyDataGropuedDTO {
    private LocalDate date;
    private String province;
    private Optional<MeteorologyData> meteorologyData;
}