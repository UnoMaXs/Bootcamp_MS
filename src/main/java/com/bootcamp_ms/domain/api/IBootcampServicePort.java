package com.bootcamp_ms.domain.api;

import com.bootcamp_ms.domain.model.Bootcamp;
import reactor.core.publisher.Mono;

public interface IBootcampServicePort {

    Mono<Bootcamp> saveBootcamp(Bootcamp bootcamp);

}
