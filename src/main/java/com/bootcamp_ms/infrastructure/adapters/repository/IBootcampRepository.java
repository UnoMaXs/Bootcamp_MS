package com.bootcamp_ms.infrastructure.adapters.repository;

import com.bootcamp_ms.infrastructure.adapters.entity.BootcampEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface IBootcampRepository extends ReactiveCrudRepository <BootcampEntity, Long> {

}
