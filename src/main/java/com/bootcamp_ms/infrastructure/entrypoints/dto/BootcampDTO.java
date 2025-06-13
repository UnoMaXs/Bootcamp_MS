package com.bootcamp_ms.infrastructure.entrypoints.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class BootcampDTO {

    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Long> capacitiesIds;

}
