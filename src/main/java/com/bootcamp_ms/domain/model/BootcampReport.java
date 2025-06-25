package com.bootcamp_ms.domain.model;

public class BootcampReport {

    private Long idBootcamp;
    private String name;
    private String description;
    private int quantityCapacities;
    private int quantityTechnologies;
    private int quantityPersons;

    public BootcampReport() {
    }

    public BootcampReport(Long idBootcamp, String name, String description, int quantityCapacities, int quantityTechnologies, int quantityPersons) {
        this.idBootcamp = idBootcamp;
        this.name = name;
        this.description = description;
        this.quantityCapacities = quantityCapacities;
        this.quantityTechnologies = quantityTechnologies;
        this.quantityPersons = quantityPersons;
    }

    public Long getIdBootcamp() {
        return idBootcamp;
    }

    public void setIdBootcamp(Long idBootcamp) {
        this.idBootcamp = idBootcamp;
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

    public int getQuantityCapacities() {
        return quantityCapacities;
    }

    public void setQuantityCapacities(int quantityCapacities) {
        this.quantityCapacities = quantityCapacities;
    }

    public int getQuantityTechnologies() {
        return quantityTechnologies;
    }

    public void setQuantityTechnologies(int quantityTechnologies) {
        this.quantityTechnologies = quantityTechnologies;
    }

    public int getQuantityPersons() {
        return quantityPersons;
    }

    public void setQuantityPersons(int quantityPersons) {
        this.quantityPersons = quantityPersons;
    }
}
