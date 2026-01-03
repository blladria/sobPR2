/*
 * Archivo: Homework2/src/main/java/deim/urv/cat/homework2/service/UserServiceImpl.java
 * Ubicación: Frontend (Cliente MVC)
 */
package deim.urv.cat.homework2.service;

import deim.urv.cat.homework2.model.User;
import deim.urv.cat.homework2.controller.UserForm;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.client.Entity;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserServiceImpl implements UserService {

    private final WebTarget webTarget;
    private final jakarta.ws.rs.client.Client client;
    private static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class.getName());

    // URL base del backend
    private static final String BASE_URI = "http://localhost:8080/PR1_sob_grup_54/rest/api/v1";

    public UserServiceImpl() {
        client = jakarta.ws.rs.client.ClientBuilder.newClient();
        // Apunta a .../api/v1/customer
        webTarget = client.target(BASE_URI).path("customer");
    }

    /**
     * Busca un usuario por username usando Basic Auth (necesario para endpoints
     * protegidos) Endpoint: GET /customer/username/{username}
     */
    public User findUserByUsername(String username, String password) {
        try {
            String authHeader = getAuthHeader(username, password);

            Response response = webTarget.path("username").path(username)
                    .request(MediaType.APPLICATION_JSON)
                    .header("Authorization", authHeader)
                    .get();

            if (response.getStatus() == 200) {
                return response.readEntity(User.class);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding user by username", e);
        }
        return null;
    }

    /**
     * Método legacy de la interfaz, redirigimos o adaptamos si es necesario.
     * Dado que el backend no tiene búsqueda por email pública, retornamos null
     * o implementamos si el backend cambia.
     */
    @Override
    public User findUserByEmail(String email) {
        // No implementado en backend actual
        return null;
    }

    @Override
    public boolean addUser(UserForm user) {
        try {
            Response response = webTarget.request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(user, MediaType.APPLICATION_JSON));

            return (response.getStatus() == 201);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating user", e);
            return false;
        }
    }

    @Override
    public User validateUser(String username, String password) {
        // Reutilizamos la lógica de búsqueda que ya valida credenciales
        return findUserByUsername(username, password);
    }

    // --- NUEVO: Método para actualizar usuario (lastConsultedModel) ---
    public boolean updateUser(User user, String password) {
        try {
            if (user.getId() == null) {
                return false;
            }

            String authHeader = getAuthHeader(user.getUsername(), password);

            Response response = webTarget.path(user.getId().toString())
                    .request(MediaType.APPLICATION_JSON)
                    .header("Authorization", authHeader)
                    .put(Entity.entity(user, MediaType.APPLICATION_JSON));

            return (response.getStatus() == 204); // 204 No Content es éxito en PUT
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating user", e);
            return false;
        }
    }

    private String getAuthHeader(String username, String password) {
        try {
            String authString = username + ":" + password;
            return "Basic " + Base64.getEncoder().encodeToString(authString.getBytes("UTF-8"));
        } catch (Exception e) {
            return "";
        }
    }
}
