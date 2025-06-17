package com.bootcamp_ms.infrastructure.adapters.client;

import com.bootcamp_ms.infrastructure.entrypoints.dto.CapacitySummaryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public Flux<CapacitySummaryDTO> getCapacitiesSummaryByIds(List<Long> ids) {
        String queryParam = ids.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        return webClient.get()
                .uri(capacityServiceUrl + "/capacity/summaries?ids=" + queryParam)
                .retrieve()
                .bodyToFlux(CapacitySummaryDTO.class);
    }

    public Flux<Long> getAllTechnologyIdsFromOtherCapacities(List<Long> excludedCapacityIds) {
        String query = excludedCapacityIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        return webClient.get()
                .uri(capacityServiceUrl + "/capacity/technologies/used?excludeIds=" + query)
                .retrieve()
                .bodyToFlux(Long.class);
    }

    public Mono<Void> deleteCapacitiesByIds(List<Long> ids) {
        String query = ids.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        return webClient.method(HttpMethod.DELETE)
                .uri(capacityServiceUrl + "/capacity/delete?ids=" + query)
                .retrieve()
                .bodyToMono(Void.class);
    }


}
