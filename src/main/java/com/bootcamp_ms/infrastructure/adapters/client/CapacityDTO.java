package com.bootcamp_ms.infrastructure.adapters.client;

import lombok.Data;

import java.util.List;

@Data
public class CapacityDTO {
    private Long id;
    private String name;
    private String description;
    private List<Long> technologyIds;
}
