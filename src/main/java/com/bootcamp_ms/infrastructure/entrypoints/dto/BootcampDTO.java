package com.bootcamp_ms.infrastructure.entrypoints.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class BootcampDTO {

    private String name;
    private String description;
    private String startDate;
    private String endDate;
    private List<Long> capacitiesIds;

}
