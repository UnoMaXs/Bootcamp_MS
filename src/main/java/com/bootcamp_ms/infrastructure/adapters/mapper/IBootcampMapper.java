package com.bootcamp_ms.infrastructure.adapters.mapper;

import com.bootcamp_ms.domain.model.Bootcamp;
import com.bootcamp_ms.infrastructure.adapters.entity.BootcampEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IBootcampMapper {

    Bootcamp toBootcamp(BootcampEntity bootcampEntity);
    BootcampEntity toBootcampEntity(Bootcamp bootcamp);

}
