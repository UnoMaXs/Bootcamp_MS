package com.bootcamp_ms.domain.spi;

import com.bootcamp_ms.domain.model.Bootcamp;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IBootcampPersistencePort {

    Mono<Bootcamp> saveBootcamp(Bootcamp bootcamp);
    Mono<List<Bootcamp>> findAll(int page, int size, String sortBy, String direction);

}
