package deim.urv.cat.homework2.service;

import deim.urv.cat.homework2.model.User;
import deim.urv.cat.homework2.controller.UserForm;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.client.Entity;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserServiceImpl implements UserService {

    private final WebTarget webTarget;
    private final jakarta.ws.rs.client.Client client;
    private static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class.getName());

    private static final String BASE_URI = "http://localhost:8080/PR1_sob_grup_54/rest/api/v1";

    public UserServiceImpl() {
        client = jakarta.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("customer");
    }

    @Override
    public User findUserByEmail(String email) {
        try {
            Response response = webTarget.path(email)
                    .request(MediaType.APPLICATION_JSON)
                    .get();
            if (response.getStatus() == 200) {
                return response.readEntity(User.class);
            }
        } catch (Exception e) {
            // Ignorar
        }
        return null;
    }

    @Override
    public boolean addUser(UserForm userForm) {
        try {
            // Usamos el User ESTÁNDAR sin añadir campos extra
            User userToSend = new User();
            userToSend.setUsername(userForm.getUsername());
            userToSend.setPassword(userForm.getPassword());
            userToSend.setEmail(userForm.getEmail());
            userToSend.setName(userForm.getName());

            Response response = webTarget.request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(userToSend, MediaType.APPLICATION_JSON));

            int status = response.getStatus();

            if (status == 201 || status == 200) {
                return true;
            } else {
                // Imprimir error en consola para depurar si falla
                String errorBody = response.readEntity(String.class);
                System.out.println("--- ERROR REGISTER --- Status: " + status + " Body: " + errorBody);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public User validateUser(String username, String password) {
        try {
            String authString = username + ":" + password;
            String authHeader = "Basic " + Base64.getEncoder().encodeToString(authString.getBytes(StandardCharsets.UTF_8));

            Response response = webTarget.path("username").path(username)
                    .request(MediaType.APPLICATION_JSON)
                    .header("Authorization", authHeader)
                    .get();

            if (response.getStatus() == 200) {
                return response.readEntity(User.class);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error validating user", e);
        }
        return null;
    }
}
