package com.bootcamp_ms.infrastructure.adapters.client;

import com.capacidad_ms.infrastructure.entrypoints.dto.TechnologySummaryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TechnologyWebClient {

    private final WebClient webClient;

    @Value("${app.technology.url}")
    private String technologyServiceUrl;

    public Mono<Map<String, Object>> validateTechnologyIds(List<Long> ids) {
        String queryParam = String.join(",", ids.stream().map(String::valueOf).toList());

        return webClient.get()
                .uri(technologyServiceUrl + "/technology/ids?ids=" + queryParam)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {});
    }

    public Flux<TechnologySummaryDTO> getTechnologySummariesByIds(List<Long> ids) {
        String queryParam = ids.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        return webClient.get()
                .uri(technologyServiceUrl + "/technology/summaries?ids=" + queryParam)
                .retrieve()
                .bodyToFlux(TechnologySummaryDTO.class);
    }


}
