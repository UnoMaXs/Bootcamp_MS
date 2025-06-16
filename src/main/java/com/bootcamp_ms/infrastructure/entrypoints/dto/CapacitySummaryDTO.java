package com.bootcamp_ms.infrastructure.entrypoints.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CapacitySummaryDTO {

    private Long id;
    private String name;
    private List<TechnologySummaryDTO> technologies;

}
