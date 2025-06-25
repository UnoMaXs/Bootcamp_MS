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

@Component
@RequiredArgsConstructor
public class BootcampHandler {

    private final IBootcampServicePort bootcampServicePort;
    private final IBootcampInfraMapper bootcampInfraMapper;

    public Mono<ServerResponse> saveBootcamp(ServerRequest request) {
        String token = request.headers().firstHeader("Authorization");

        return request.bodyToMono(BootcampDTO.class)
                .flatMap(dto -> {
                    Bootcamp bootcamp = bootcampInfraMapper.toBootcamp(dto);
                    return bootcampServicePort.saveBootcamp(bootcamp, token);
                })
                .flatMap(savedBootcamp ->
                        ServerResponse.ok()
                                .bodyValue(savedBootcamp)
                );
    }

    public Mono<ServerResponse> listBootcamps(ServerRequest request) {
        int page = Integer.parseInt(request.queryParam("page").orElse("0"));
        int size = Integer.parseInt(request.queryParam("size").orElse("10"));
        String sortBy = request.queryParam("sortBy").orElse("name");
        String direction = request.queryParam("direction").orElse("asc");
        String token = request.headers().firstHeader("Authorization");

        return bootcampServicePort.findAll(page, size, sortBy, direction, token)
                .flatMap(list -> ServerResponse.ok().bodyValue(list));
    }

    public Mono<ServerResponse> deleteBootcamp(ServerRequest request) {
        Long id = Long.parseLong(request.queryParam("id").orElseThrow(() ->
                new IllegalArgumentException("El parámetro 'id' es obligatorio")));
        String token = request.headers().firstHeader("Authorization");

        return bootcampServicePort.deleteBootcamp(id,token)
                .then(ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> getBootcampById(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));

        return bootcampServicePort.findById(id)
                .map(bootcampInfraMapper::toBootcampDTO)
                .flatMap(dto -> ServerResponse.ok().bodyValue(dto))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> getMostPopularBootcamp(ServerRequest request) {
        String token = request.headers().firstHeader("Authorization");
        return bootcampServicePort.findBootcampWithMostPersons(token)
                .flatMap(dto -> ServerResponse.ok().bodyValue(dto))
                .switchIfEmpty(ServerResponse.noContent().build());
    }

}
