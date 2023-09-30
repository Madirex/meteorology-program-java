package com.madiben.models.dto;

import com.madiben.models.MeteorologyData;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * Clase MeteorologyDataGropuedDTO que representa los datos de meteorología agrupados por fecha y provincia
 */
@Data
@Builder
public class MeteorologyDataGroupedDTO {
    private LocalDate date;
    private String province;
    private List<MeteorologyData> meteorologyData;
}