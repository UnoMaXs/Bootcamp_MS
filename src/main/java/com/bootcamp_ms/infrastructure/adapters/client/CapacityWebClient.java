package com.bootcamp_ms.infrastructure.adapters.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CapacityWebClient {

    private final WebClient webClient;

    @Value("${app.capacity.url}")
    private String capacityServiceUrl;

    public Mono<Map<String, Object>> validateCapacityIds(List<Long> ids) {
        String queryParam = String.join(",", ids.stream().map(String::valueOf).toList());

        return webClient.get()
                .uri(capacityServiceUrl + "/capacity/ids?ids=" + queryParam)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {});
    }

//    public Flux<TechnologySummaryDTO> getTechnologySummariesByIds(List<Long> ids) {
//        String queryParam = ids.stream()
//                .map(String::valueOf)
//                .collect(Collectors.joining(","));
//
//        return webClient.get()
//                .uri(capacityServiceUrl + "/technology/summaries?ids=" + queryParam)
//                .retrieve()
//                .bodyToFlux(TechnologySummaryDTO.class);
//    }

}
