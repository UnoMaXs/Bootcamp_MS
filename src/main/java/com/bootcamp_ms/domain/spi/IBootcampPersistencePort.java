package com.bootcamp_ms.domain.spi;

import com.bootcamp_ms.domain.model.Bootcamp;
import reactor.core.publisher.Mono;

public interface IBootcampPersistencePort {

    Mono<Bootcamp> saveBootcamp(Bootcamp bootcamp);

}
