package deim.urv.cat.homework2.service;

import deim.urv.cat.homework2.model.User;
import deim.urv.cat.homework2.controller.UserForm;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class UserServiceImpl implements UserService {

    private final WebTarget webTarget;
    private final Client client;
    // URL de tu Backend (Práctica 1)
    private static final String BASE_URI = "http://localhost:8080/PR1_sob_grup_54/rest/api/v1";

    public UserServiceImpl() {
        this.client = ClientBuilder.newClient();
        this.webTarget = client.target(BASE_URI).path("customer");
    }

    // Generación robusta de cabecera Basic Auth
    private String getAuthHeader(String username, String password) {
        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
        return "Basic " + new String(encodedAuth);
    }

    @Override
    public User validateUser(String username, String password) {
        if (username == null || password == null) {
            return null;
        }

        try {
            Response response = client.target(BASE_URI)
                    .path("customer")
                    .path(username)
                    .request(MediaType.APPLICATION_JSON)
                    .header("Authorization", getAuthHeader(username, password))
                    .get();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                User user = response.readEntity(User.class);
                user.setPassword(password); // Guardamos la pass para el objeto de sesión
                return user;
            }
        } catch (Exception e) {
            // Log de error opcional
        }
        return null;
    }

    @Override
    public User getUser(String username, String password) {
        return validateUser(username, password);
    }

    @Override
    public boolean addUser(UserForm userForm) {
        try {
            Response response = webTarget.request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(userForm, MediaType.APPLICATION_JSON));
            return response.getStatus() == Response.Status.CREATED.getStatusCode();
        } catch (Exception e) {
            return false;
        }
    }

    // Método de actualización (utilizado por el ModelController para actualizar el ID del último modelo)
    public void updateUser(User user, String password) {
        try {
            client.target(BASE_URI)
                    .path("customer")
                    .path(user.getUsername())
                    .request(MediaType.APPLICATION_JSON)
                    .header("Authorization", getAuthHeader(user.getUsername(), password))
                    .put(Entity.entity(user, MediaType.APPLICATION_JSON));
        } catch (Exception e) {
            // Error silencioso
        }
    }
}
