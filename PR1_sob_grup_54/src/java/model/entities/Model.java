/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.entities;

import java.io.Serializable;
import java.util.List;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author bllad
 */
@Entity
@XmlRootElement
@Table(name = "IAS")
public class Model implements Serializable {

    private static final long serialVersionUID = 1L;

    // Configuración del ID por Secuencia
    @Id
    @SequenceGenerator(name = "Model_Gen", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Model_Gen")
    private Long id;

    private String logo; 

    private String mainCapability; // Capacidad principal

    @Column(length = 1000) // Longitud suficiente para el resumen
    private String summary; // Resumen de 20-30 palabras

    @Basic(optional = false)
    @Column(nullable = false, unique = true)
    private String name;

    @Basic(optional = false)
    @Column(nullable = false)
    private String provider; // OpenAI, Anthropic, etc.

    private String version;

    @Lob
    private String description;

    // Usamos @ElementCollection para las listas de capacidades
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "MODEL_CAPABILITIES", joinColumns = @JoinColumn(name = "MODEL_ID"))
    @Column(name = "CAPABILITY")
    private List<String> capabilities;

    // Campos de especificación
    private String license; // e.g., Custom, Apache 2.0
    private Integer contextLength;
    private Double qualityIndex;

    private String trainingDataDate;

    private String lastUpdated;

    private String inputType;
    private String outputType;
    private String publisher;
    private String languages;

    // Para modelos privados que requieren autenticación
    private boolean isPrivate;

    // Requerido vacío por JPA/JAX-RS para la persistencia y la deserialización JSON
    public Model() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
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

    public boolean isPrivate() {
        return isPrivate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    // Atributo description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Atributo capabilities
    public List<String> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(List<String> capabilities) {
        this.capabilities = capabilities;
    }

    // Atributo license
    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getTrainingDataDate() {
        return trainingDataDate;
    }

    public void setTrainingDataDate(String trainingDataDate) {
        this.trainingDataDate = trainingDataDate;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public String getOutputType() {
        return outputType;
    }

    public void setOutputType(String outputType) {
        this.outputType = outputType;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getMainCapability() {
        return mainCapability;
    }

    public void setMainCapability(String mainCapability) {
        this.mainCapability = mainCapability;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

}
