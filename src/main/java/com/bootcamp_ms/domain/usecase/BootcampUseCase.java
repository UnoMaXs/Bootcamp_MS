package com.bootcamp_ms.domain.usecase;

import com.bootcamp_ms.domain.api.IBootcampServicePort;
import com.bootcamp_ms.domain.model.Bootcamp;
import com.bootcamp_ms.domain.spi.IBootcampPersistencePort;
import com.bootcamp_ms.infrastructure.adapters.client.CapacityWebClient;
import com.bootcamp_ms.infrastructure.entrypoints.dto.BootcampResponseDTO;
import com.bootcamp_ms.infrastructure.entrypoints.dto.CapacitySummaryDTO;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@RequiredArgsConstructor
public class BootcampUseCase implements IBootcampServicePort {

    private final IBootcampPersistencePort bootcampPersistencePort;
    private final CapacityWebClient capacityWebClient;

    @Override
    public Mono<Bootcamp> saveBootcamp(Bootcamp bootcamp) {
        return bootcampPersistencePort.saveBootcamp(bootcamp);
    }

    @Override
    public Mono<List<BootcampResponseDTO>> findAll(int page, int size, String sortBy, String direction) {
        return bootcampPersistencePort.findAll(page, size, "id", "asc")
                .flatMap(bootcampList -> {
                    List<Long> allCapacityIds = bootcampList.stream()
                            .flatMap(b -> b.getCapacitiesIds().stream())
                            .distinct()
                            .toList();

                    return capacityWebClient.getCapacitiesSummaryByIds(allCapacityIds)
                            .collectMap(CapacitySummaryDTO::getId, Function.identity())
                            .map(capMap -> {
                                List<BootcampResponseDTO> responseList = bootcampList.stream().map(b -> {
                                    List<CapacitySummaryDTO> capacities = b.getCapacitiesIds().stream()
                                            .map(capMap::get)
                                            .filter(Objects::nonNull)
                                            .toList();

                                    return new BootcampResponseDTO(
                                            b.getId(),
                                            b.getName(),
                                            b.getDescription(),
                                            capacities
                                    );
                                }).toList();

                                if (sortBy.equalsIgnoreCase("capacityCount")) {
                                    responseList = responseList.stream()
                                            .sorted((a, b) -> direction.equalsIgnoreCase("asc") ?
                                                    Integer.compare(a.getCapacities().size(), b.getCapacities().size()) :
                                                    Integer.compare(b.getCapacities().size(), a.getCapacities().size()))
                                            .toList();
                                } else if (sortBy.equalsIgnoreCase("name")) {
                                    responseList = responseList.stream()
                                            .sorted((a, b) -> direction.equalsIgnoreCase("asc") ?
                                                    a.getName().compareToIgnoreCase(b.getName()) :
                                                    b.getName().compareToIgnoreCase(a.getName()))
                                            .toList();
                                }

                                return responseList;
                            });
                });
    }


}
