package deim.urv.cat.homework2.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class User implements Serializable {

    private Long id;
    private String name;
    private String username;
    private String email;
    private String password;
    private Long lastConsultedModelId;
    private List<Map<String, String>> links; // Para HATEOAS

    public User() {
    }

    // Getters y Setters coincidiendo exactamente con Customer.java
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getLastConsultedModelId() {
        return lastConsultedModelId;
    }

    public void setLastConsultedModelId(Long lastConsultedModelId) {
        this.lastConsultedModelId = lastConsultedModelId;
    }

    public List<Map<String, String>> getLinks() {
        return links;
    }

    public void setLinks(List<Map<String, String>> links) {
        this.links = links;
    }
}
