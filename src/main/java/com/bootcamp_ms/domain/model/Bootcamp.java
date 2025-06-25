package com.bootcamp_ms.domain.model;

import java.time.LocalDate;
import java.util.List;

public class Bootcamp {

    private Long id;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Long> capacitiesIds;

    public Bootcamp() {
    }

    public Bootcamp(Long id, String name, String description, LocalDate startDate, LocalDate endDate, List<Long> capacitiesIds) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.capacitiesIds = capacitiesIds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public List<Long> getCapacitiesIds() {
        return capacitiesIds;
    }

    public void setCapacitiesIds(List<Long> capacitiesIds) {
        this.capacitiesIds = capacitiesIds;
    }

}
