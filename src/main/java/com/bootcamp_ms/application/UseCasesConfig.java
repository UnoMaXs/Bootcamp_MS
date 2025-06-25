package com.bootcamp_ms.application;

import com.bootcamp_ms.domain.api.IBootcampServicePort;
import com.bootcamp_ms.domain.spi.IBootcampPersistencePort;
import com.bootcamp_ms.domain.spi.IBootcampReportPersistencePort;
import com.bootcamp_ms.domain.usecase.BootcampUseCase;
import com.bootcamp_ms.infrastructure.adapters.client.CapacityWebClient;
import com.bootcamp_ms.infrastructure.adapters.client.TechnologyWebClient;
import com.bootcamp_ms.infrastructure.adapters.mapper.IBootcampMapper;
import com.bootcamp_ms.infrastructure.adapters.persistence.BootcampAdapter;
import com.bootcamp_ms.infrastructure.adapters.repository.IBootcampRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class UseCasesConfig {

    private final IBootcampRepository bootcampRepository;
    private final IBootcampMapper bootcampMapper;


    @Bean
    public IBootcampPersistencePort bootcampPersistencePort() {
        return new BootcampAdapter(bootcampRepository, bootcampMapper);
    }

    @Bean
    public IBootcampServicePort bootcampServicePort(IBootcampPersistencePort bootcampPersistencePort,
                                                    CapacityWebClient capacityWebClient,
                                                    TechnologyWebClient technologyWebClient,
                                                    IBootcampReportPersistencePort bootcampReportPersistencePort) {
        return new BootcampUseCase(bootcampPersistencePort, capacityWebClient,
                technologyWebClient, bootcampReportPersistencePort);
    }

}
