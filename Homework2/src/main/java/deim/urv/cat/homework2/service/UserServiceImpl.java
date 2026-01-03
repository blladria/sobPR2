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

    private final Client client;
    private static final String BASE_URI = "http://localhost:8080/PR1_sob_grup_54/rest/api/v1";

    public UserServiceImpl() {
        this.client = ClientBuilder.newClient();
    }

    private String getAuthHeader(String username, String password) {
        String auth = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
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

            if (response.getStatus() == 200) {
                User user = response.readEntity(User.class);
                user.setPassword(password);
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User getUser(String username, String password) {
        return validateUser(username, password);
    }

    @Override
    public boolean addUser(UserForm userForm) {
        User newUser = new User();
        newUser.setName(userForm.getName());
        newUser.setUsername(userForm.getUsername());
        newUser.setEmail(userForm.getEmail());
        newUser.setPassword(userForm.getPassword());

        try {
            Response response = client.target(BASE_URI)
                    .path("customer")
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(newUser, MediaType.APPLICATION_JSON));
            return response.getStatus() == Response.Status.CREATED.getStatusCode();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void updateUser(User user, String password) {
        try {
            client.target(BASE_URI)
                    .path("customer")
                    .path(user.getUsername())
                    .request(MediaType.APPLICATION_JSON)
                    .header("Authorization", getAuthHeader(user.getUsername(), password))
                    .put(Entity.entity(user, MediaType.APPLICATION_JSON));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
