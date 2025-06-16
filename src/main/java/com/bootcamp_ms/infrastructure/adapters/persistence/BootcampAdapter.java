package com.bootcamp_ms.infrastructure.adapters.persistence;

import com.bootcamp_ms.domain.model.Bootcamp;
import com.bootcamp_ms.domain.spi.IBootcampPersistencePort;
import com.bootcamp_ms.infrastructure.adapters.mapper.IBootcampMapper;
import com.bootcamp_ms.infrastructure.adapters.repository.IBootcampRepository;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;

@AllArgsConstructor
public class BootcampAdapter implements IBootcampPersistencePort {

    private final IBootcampRepository bootcampRepository;
    private final IBootcampMapper bootcampMapper;

    @Override
    public Mono<Bootcamp> saveBootcamp(Bootcamp bootcamp) {
        return Mono.just(bootcamp)
                .map(bootcampMapper::toBootcampEntity)
                .flatMap(bootcampRepository::save)
                .map(bootcampMapper::toBootcamp);
    }

    @Override
    public Mono<List<Bootcamp>> findAll(int page, int size, String sortBy, String direction) {
        return bootcampRepository.findAll()
                .map(bootcampMapper::toBootcamp)
                .collectList();
    }

}
