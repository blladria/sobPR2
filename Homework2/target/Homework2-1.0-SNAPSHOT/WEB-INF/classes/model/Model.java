/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deim.urv.cat.homework2.model;

import jakarta.json.bind.annotation.JsonbProperty;
import java.io.Serializable;
import java.util.List;

public class Model implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String provider; 
    private String logo; 
    private String mainCapability;
    private String summary;
    private String version;
    private String description;
    private List<String> capabilities;
    
    // Campos de especificación
    private String license;
    private Integer contextLength;
    private Double qualityIndex;
    private String trainingDataDate;
    private String lastUpdated;
    private String inputType;
    private String outputType;
    private String publisher;
    private String languages;
    
    // Control de privacidad
    // Mapea tanto si el JSON trae "private":true como "isPrivate":true
    @JsonbProperty("private")
    private boolean isPrivate;

    // CONSTRUCTOR VACÍO (Obligatorio para JAX-RS)
    public Model() {
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return fixNull(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvider() {
        return fixNull(provider);
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getLogo() {
        return fixNull(logo);
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getMainCapability() {
        return fixNull(mainCapability);
    }

    public void setMainCapability(String mainCapability) {
        this.mainCapability = mainCapability;
    }

    public String getSummary() {
        return fixNull(summary);
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getVersion() {
        return fixNull(version);
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return fixNull(description);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(List<String> capabilities) {
        this.capabilities = capabilities;
    }

    public String getLicense() {
        return fixNull(license);
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public Integer getContextLength() {
        return contextLength;
    }

    public void setContextLength(Integer contextLength) {
        this.contextLength = contextLength;
    }

    public Double getQualityIndex() {
        return qualityIndex;
    }

    public void setQualityIndex(Double qualityIndex) {
        this.qualityIndex = qualityIndex;
    }

    public String getTrainingDataDate() {
        return fixNull(trainingDataDate);
    }

    public void setTrainingDataDate(String trainingDataDate) {
        this.trainingDataDate = trainingDataDate;
    }

    public String getLastUpdated() {
        return fixNull(lastUpdated);
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getInputType() {
        return fixNull(inputType);
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public String getOutputType() {
        return fixNull(outputType);
    }

    public void setOutputType(String outputType) {
        this.outputType = outputType;
    }

    public String getPublisher() {
        return fixNull(publisher);
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getLanguages() {
        return fixNull(languages);
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    // Getter estándar para Boolean
    public boolean isPrivate() {
        return isPrivate;
    }
    
    // Setter estándar
    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    // Helper para evitar nulls en la vista
    private String fixNull(String in) {
        return (in == null) ? "" : in;
    }
}