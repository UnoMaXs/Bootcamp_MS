package com.bootcamp_ms.infrastructure.adapters.persistence;

import com.bootcamp_ms.domain.model.Bootcamp;
import com.bootcamp_ms.domain.spi.IBootcampPersistencePort;
import com.bootcamp_ms.infrastructure.adapters.mapper.IBootcampMapper;
import com.bootcamp_ms.infrastructure.adapters.repository.IBootcampRepository;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
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

    @Override
    public Mono<Bootcamp> findById(Long id) {
        return bootcampRepository.findById(id)
                .map(bootcampMapper::toBootcamp);
    }

    @Override
    public Flux<Bootcamp> findAllExcept(Long bootcampId) {
        return bootcampRepository.findAll()
                .filter(bootcamp -> !bootcamp.getId().equals(bootcampId))
                .map(bootcampMapper::toBootcamp);
    }

    @Override
    public Mono<Void> deleteBootcampById(Long bootcampId) {
        return bootcampRepository.deleteById(bootcampId);
    }

}

