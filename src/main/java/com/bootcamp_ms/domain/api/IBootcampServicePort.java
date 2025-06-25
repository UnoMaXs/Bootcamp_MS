package com.bootcamp_ms.domain.api;

import com.bootcamp_ms.domain.model.Bootcamp;
import com.bootcamp_ms.infrastructure.entrypoints.dto.BootcampResponseDTO;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IBootcampServicePort {

    Mono<Bootcamp> saveBootcamp(Bootcamp bootcamp, String token);
    Mono<List<BootcampResponseDTO>> findAll(int page, int size, String sortBy, String direction, String token);
    Mono<Void> deleteBootcamp(Long bootcampId, String token);
    Mono<Bootcamp> findById(Long id);
    Mono<BootcampResponseDTO> findBootcampWithMostPersons(String token);




}
