/*
 * Archivo: Homework2/src/main/java/deim/urv/cat/homework2/model/Model.java
 */
package deim.urv.cat.homework2.model;

import java.io.Serializable;
import java.util.List;

public class Model implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String provider;
    private String mainCapability;
    private List<String> capabilities;
    private boolean isPrivate; // Cuidado con la convención de nombres JSON (a veces es 'private')

    // --- NUEVOS CAMPOS ---
    private String description;
    private String version;
    private String contextWindow; // String para permitir "128k" etc.
    private Integer qualityIndex;
    private String license;
    private String lastUpdated;

    public Model() {
    }

    // --- GETTERS Y SETTERS ---
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

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getMainCapability() {
        return mainCapability;
    }

    public void setMainCapability(String mainCapability) {
        this.mainCapability = mainCapability;
    }

    public List<String> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(List<String> capabilities) {
        this.capabilities = capabilities;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    // Nuevos
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getContextWindow() {
        return contextWindow;
    }

    public void setContextWindow(String contextWindow) {
        this.contextWindow = contextWindow;
    }

    public Integer getQualityIndex() {
        return qualityIndex;
    }

    public void setQualityIndex(Integer qualityIndex) {
        this.qualityIndex = qualityIndex;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
