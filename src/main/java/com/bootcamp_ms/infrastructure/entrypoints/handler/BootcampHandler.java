package com.bootcamp_ms.infrastructure.entrypoints.handler;

import com.bootcamp_ms.domain.api.IBootcampServicePort;
import com.bootcamp_ms.domain.model.Bootcamp;
import com.bootcamp_ms.infrastructure.entrypoints.dto.BootcampDTO;
import com.bootcamp_ms.infrastructure.entrypoints.mapper.IBootcampInfraMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BootcampHandler {

    private final IBootcampServicePort bootcampServicePort;
    private final IBootcampInfraMapper bootcampInfraMapper;

    public Mono<ServerResponse> saveBootcamp(ServerRequest request) {
        return request.bodyToMono(BootcampDTO.class)
                .flatMap(dto -> {
                    Bootcamp bootcamp = bootcampInfraMapper.toBootcamp(dto);
                    return bootcampServicePort.saveBootcamp(bootcamp);
                })
                .flatMap(savedBootcamp ->
                        ServerResponse.ok()
                                .bodyValue(savedBootcamp)
                );
    }

}
