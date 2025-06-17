package com.bootcamp_ms.domain.api;

import com.bootcamp_ms.domain.model.Bootcamp;
import com.bootcamp_ms.infrastructure.entrypoints.dto.BootcampResponseDTO;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IBootcampServicePort {

    Mono<Bootcamp> saveBootcamp(Bootcamp bootcamp);
    Mono<List<BootcampResponseDTO>> findAll(int page, int size, String sortBy, String direction);
    Mono<Void> deleteBootcamp(Long bootcampId);
    Mono<Bootcamp> findById(Long id);



}
