package com.bootcamp_ms.infrastructure.adapters.client;

import lombok.Data;

@Data
public class BootcampReportDTO {

    private Long idBootcamp;
    private String name;
    private String description;
    private int quantityCapacities;
    private int quantityTechnologies;
    private int quantityPersons;

}
