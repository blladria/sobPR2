/*
 * Archivo: Homework2/src/main/java/deim/urv/cat/homework2/model/User.java
 * Ubicación: Frontend (Cliente MVC)
 */
package deim.urv.cat.homework2.model;

import jakarta.ws.rs.FormParam;
import java.io.Serializable;
import java.util.Map;

public class User implements Serializable {

    private Long id;

    @FormParam("username")
    private String username;

    @FormParam("password")
    private String password;

    @FormParam("name")
    private String name;

    @FormParam("email")
    private String email;

    // Campo para recibir el HATEOAS link del JSON
    private Map<String, String> links;

    // Campo para guardar localmente el ID (opcional, pero útil)
    private Long lastConsultedModelId;

    public User() {
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Map<String, String> getLinks() {
        return links;
    }

    public void setLinks(Map<String, String> links) {
        this.links = links;
    }

    public Long getLastConsultedModelId() {
        return lastConsultedModelId;
    }

    public void setLastConsultedModelId(Long lastConsultedModelId) {
        this.lastConsultedModelId = lastConsultedModelId;
    }
}
