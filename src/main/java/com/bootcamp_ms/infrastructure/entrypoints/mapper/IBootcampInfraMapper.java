package com.bootcamp_ms.infrastructure.entrypoints.mapper;

import com.bootcamp_ms.domain.model.Bootcamp;
import com.bootcamp_ms.infrastructure.entrypoints.dto.BootcampDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IBootcampInfraMapper {

    Bootcamp toBootcamp(BootcampDTO bootcampDTO);
    BootcampDTO toBootcampDTO(Bootcamp bootcamp);

}
