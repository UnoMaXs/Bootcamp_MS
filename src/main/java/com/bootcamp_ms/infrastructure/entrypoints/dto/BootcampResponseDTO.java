package com.bootcamp_ms.infrastructure.entrypoints.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BootcampResponseDTO {

    private Long id;
    private String name;
    private String description;
    private List<CapacitySummaryDTO> capacities;

}
