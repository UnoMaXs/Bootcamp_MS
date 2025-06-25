package com.bootcamp_ms.infrastructure.adapters.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BootcampReportWebClient {

    private final WebClient webClient;

    @Value("${app.report.url}")
    private String reportServiceUrl;

    public Mono<Void> sendReport(BootcampReportDTO dto, String token) {
        return webClient.post()
                .uri(reportServiceUrl + "/report/bootcamp")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(Void.class)
                .onErrorResume(e -> Mono.empty());
    }
    public Mono<BootcampReportDTO> findMostPopularBootcamp(String token) {
        return webClient.get()
                .uri(reportServiceUrl + "/report/bootcamp/popular")
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(BootcampReportDTO.class);
    }

}
