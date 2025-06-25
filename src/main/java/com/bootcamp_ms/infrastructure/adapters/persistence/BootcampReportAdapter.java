package com.bootcamp_ms.infrastructure.adapters.persistence;

import com.bootcamp_ms.domain.model.BootcampReport;
import com.bootcamp_ms.domain.spi.IBootcampReportPersistencePort;
import com.bootcamp_ms.infrastructure.adapters.client.BootcampReportDTO;
import com.bootcamp_ms.infrastructure.adapters.client.BootcampReportWebClient;
import com.bootcamp_ms.infrastructure.entrypoints.mapper.IBootcampReportInfraMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BootcampReportAdapter implements IBootcampReportPersistencePort {

    private final BootcampReportWebClient reportWebClient;
    private final IBootcampReportInfraMapper bootcampReportInfraMapper;


    @Override
    public Mono<Void> sendBootcampReport(BootcampReport bootcampReport, String token) {
        BootcampReportDTO dto = bootcampReportInfraMapper.toBootcampReportDTO(bootcampReport);
        return reportWebClient.sendReport(dto, token);
    }

    @Override
    public Mono<BootcampReport> findBootcampWithMostPersons(String token) {
        return reportWebClient.findMostPopularBootcamp(token)
                .map(bootcampReportInfraMapper::toBootcampReport);
    }

}
