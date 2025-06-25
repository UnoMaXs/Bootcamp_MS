package com.bootcamp_ms.infrastructure.entrypoints.mapper;

import com.bootcamp_ms.domain.model.BootcampReport;
import com.bootcamp_ms.infrastructure.adapters.client.BootcampReportDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IBootcampReportInfraMapper {

    BootcampReportDTO toBootcampReportDTO(BootcampReport bootcampReport);
    BootcampReport toBootcampReport(BootcampReportDTO dto);


}
