package com.bootcamp_ms.domain.usecase;

import com.bootcamp_ms.domain.api.IBootcampServicePort;
import com.bootcamp_ms.domain.model.Bootcamp;
import com.bootcamp_ms.domain.spi.IBootcampPersistencePort;
import com.bootcamp_ms.infrastructure.adapters.client.CapacityWebClient;
import com.bootcamp_ms.infrastructure.adapters.client.TechnologyWebClient;
import com.bootcamp_ms.infrastructure.entrypoints.dto.BootcampResponseDTO;
import com.bootcamp_ms.infrastructure.entrypoints.dto.CapacitySummaryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.HashSet;


@RequiredArgsConstructor
public class BootcampUseCase implements IBootcampServicePort {

    private final IBootcampPersistencePort bootcampPersistencePort;
    private final CapacityWebClient capacityWebClient;
    private final TechnologyWebClient technologyWebClient;

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

    @Override
    public Mono<Void> deleteBootcamp(Long bootcampId) {
        return bootcampPersistencePort.findById(bootcampId)
                .switchIfEmpty(Mono.error(new ChangeSetPersister.NotFoundException()))
                .flatMap(bootcamp -> {
                    List<Long> capacityIds = bootcamp.getCapacitiesIds();

                    return bootcampPersistencePort.findAllExcept(bootcampId)
                            .flatMap(b -> Flux.fromIterable(b.getCapacitiesIds()))
                            .collect(Collectors.toCollection(HashSet::new))
                            .flatMap(otherCapacities -> {
                                List<Long> exclusiveCapacityIds = capacityIds.stream()
                                        .filter(id -> !otherCapacities.contains(id))
                                        .toList();

                                return capacityWebClient.getCapacitiesSummaryByIds(exclusiveCapacityIds)
                                        .collectList()
                                        .flatMap(capSummaries -> {
                                            List<Long> techIds = capSummaries.stream()
                                                    .flatMap(cap -> cap.getTechnologies().stream().map(tech -> tech.getId()))
                                                    .distinct()
                                                    .toList();

                                            return capacityWebClient.getAllTechnologyIdsFromOtherCapacities(exclusiveCapacityIds)
                                                    .collect(Collectors.toSet())
                                                    .flatMap(usedTechs -> {
                                                        List<Long> techsToDelete = techIds.stream()
                                                                .filter(id -> !usedTechs.contains(id))
                                                                .toList();

                                                        return deleteCascadeOperations(
                                                                bootcampId,
                                                                exclusiveCapacityIds,
                                                                techsToDelete
                                                        );
                                                    });
                                        });
                            });
                });
    }

    @Override
    public Mono<Bootcamp> findById(Long id) {
        return bootcampPersistencePort.findById(id);
    }

    private Mono<Void> deleteCascadeOperations(Long bootcampId, List<Long> capacityIds, List<Long> technologyIds) {
        return Mono.defer(() -> {
            Mono<Void> deleteTechnologies = technologyWebClient.deleteTechnologiesByIds(technologyIds);
            Mono<Void> deleteCapacities = capacityWebClient.deleteCapacitiesByIds(capacityIds);
            Mono<Void> deleteBootcamp = bootcampPersistencePort.deleteBootcampById(bootcampId);

            return deleteTechnologies
                    .then(deleteCapacities)
                    .then(deleteBootcamp);
        }).onErrorResume(ex -> {
            return Mono.error(new RuntimeException("Fallo en la eliminación transaccional", ex));
        });
    }

}
