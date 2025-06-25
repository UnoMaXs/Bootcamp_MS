package com.bootcamp_ms.domain.spi;

import com.bootcamp_ms.domain.model.BootcampReport;
import reactor.core.publisher.Mono;

public interface IBootcampReportPersistencePort {

    Mono<Void> sendBootcampReport(BootcampReport bootcampReport, String token);
    Mono<BootcampReport> findBootcampWithMostPersons(String token);


}
