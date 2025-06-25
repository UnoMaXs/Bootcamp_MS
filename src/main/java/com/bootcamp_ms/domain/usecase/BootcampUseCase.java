package com.bootcamp_ms.domain.usecase;

import com.bootcamp_ms.domain.api.IBootcampServicePort;
import com.bootcamp_ms.domain.model.Bootcamp;
import com.bootcamp_ms.domain.model.BootcampReport;
import com.bootcamp_ms.domain.spi.IBootcampPersistencePort;
import com.bootcamp_ms.domain.spi.IBootcampReportPersistencePort;
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
    private final IBootcampReportPersistencePort bootcampReportPersistencePort;


    @Override
    public Mono<Bootcamp> saveBootcamp(Bootcamp bootcamp, String token) {
        return bootcampPersistencePort.saveBootcamp(bootcamp)
                .flatMap(saved -> {
                    List<Long> capacityIds = saved.getCapacitiesIds();

                    return capacityWebClient.getCapacitiesSummaryByIds(capacityIds, token)
                            .collectList()
                            .flatMap(capSummaries -> {
                                int technologyCount = capSummaries.stream()
                                        .flatMap(c -> c.getTechnologies().stream())
                                        .map(t -> t.getId())
                                        .collect(Collectors.toSet())
                                        .size();

                                BootcampReport report = new BootcampReport();
                                report.setIdBootcamp(saved.getId());
                                report.setName(saved.getName());
                                report.setDescription(saved.getDescription());
                                report.setQuantityCapacities(capacityIds.size());
                                report.setQuantityTechnologies(technologyCount);
                                report.setQuantityPersons(0);

                                return bootcampReportPersistencePort.sendBootcampReport(report, token)
                                        .thenReturn(saved);
                            });
                });
    }

    @Override
    public Mono<List<BootcampResponseDTO>> findAll(int page, int size, String sortBy, String direction, String token) {
        return bootcampPersistencePort.findAll(page, size, "id", "asc")
                .flatMap(bootcampList -> {
                    List<Long> allCapacityIds = bootcampList.stream()
                            .flatMap(b -> b.getCapacitiesIds().stream())
                            .distinct()
                            .toList();

                    return capacityWebClient.getCapacitiesSummaryByIds(allCapacityIds, token)
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
    public Mono<Void> deleteBootcamp(Long bootcampId, String token) {
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

                                return capacityWebClient.getCapacitiesSummaryByIds(exclusiveCapacityIds, token)
                                        .collectList()
                                        .flatMap(capSummaries -> {
                                            List<Long> techIds = capSummaries.stream()
                                                    .flatMap(cap -> cap.getTechnologies().stream().map(tech -> tech.getId()))
                                                    .distinct()
                                                    .toList();

                                            return capacityWebClient.getAllTechnologyIdsFromOtherCapacities(exclusiveCapacityIds, token)
                                                    .collect(Collectors.toSet())
                                                    .flatMap(usedTechs -> {
                                                        List<Long> techsToDelete = techIds.stream()
                                                                .filter(id -> !usedTechs.contains(id))
                                                                .toList();

                                                        return deleteCascadeOperations(
                                                                bootcampId,
                                                                exclusiveCapacityIds,
                                                                techsToDelete,
                                                                token
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

    @Override
    public Mono<BootcampResponseDTO> findBootcampWithMostPersons(String token) {
        return bootcampReportPersistencePort.findBootcampWithMostPersons(token)
                .flatMap(report ->
                        bootcampPersistencePort.findById(report.getIdBootcamp())
                                .flatMap(bootcamp -> {
                                    List<Long> capacityIds = bootcamp.getCapacitiesIds();
                                    return capacityWebClient.getCapacitiesSummaryByIds(capacityIds, token)
                                            .collectList()
                                            .map(capacitySummaries -> new BootcampResponseDTO(
                                                    bootcamp.getId(),
                                                    bootcamp.getName(),
                                                    bootcamp.getDescription(),
                                                    capacitySummaries
                                            ));
                                })
                                .switchIfEmpty(Mono.error(new RuntimeException("Bootcamp no encontrado")))
                );
    }

    private Mono<Void> deleteCascadeOperations(Long bootcampId, List<Long> capacityIds, List<Long> technologyIds, String token) {
        return Mono.defer(() -> {
            Mono<Void> deleteTechnologies = technologyWebClient.deleteTechnologiesByIds(technologyIds, token);
            Mono<Void> deleteCapacities = capacityWebClient.deleteCapacitiesByIds(capacityIds,token);
            Mono<Void> deleteBootcamp = bootcampPersistencePort.deleteBootcampById(bootcampId);

            return deleteTechnologies
                    .then(deleteCapacities)
                    .then(deleteBootcamp);
        }).onErrorResume(ex -> {
            return Mono.error(new RuntimeException("Fallo en la eliminación transaccional", ex));
        });
    }

}
