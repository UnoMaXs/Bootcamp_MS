package com.bootcamp_ms.domain.usecase;

import com.bootcamp_ms.domain.api.IBootcampServicePort;
import com.bootcamp_ms.domain.model.Bootcamp;
import com.bootcamp_ms.domain.spi.IBootcampPersistencePort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class BootcampUseCase implements IBootcampServicePort {
    //-un bootcamp debe tener como minimo 1 capacidad asociadas y como maximo 4."
    private final IBootcampPersistencePort bootcampPersistencePort;

    @Override
    public Mono<Bootcamp> saveBootcamp(Bootcamp bootcamp) {
        return bootcampPersistencePort.saveBootcamp(bootcamp);
    }

}
