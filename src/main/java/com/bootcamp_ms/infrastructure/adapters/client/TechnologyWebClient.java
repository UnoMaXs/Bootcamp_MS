package com.bootcamp_ms.infrastructure.adapters.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TechnologyWebClient {

    private final WebClient webClient;

    @Value("${app.technology.url}")
    private String technologyServiceUrl;

    public Mono<Void> deleteTechnologiesByIds(List<Long> ids, String token) {
        String query = ids.stream().map(String::valueOf).collect(Collectors.joining(","));
        return webClient.delete()
                .uri(technologyServiceUrl + "/technology/delete?ids=" + query)
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(Void.class);
    }

}
