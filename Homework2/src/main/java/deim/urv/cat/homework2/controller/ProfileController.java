/*
 * Archivo: Homework2/src/main/java/deim/urv/cat/homework2/controller/ProfileController.java
 * Ubicación: Frontend (Cliente MVC)
 */
package deim.urv.cat.homework2.controller;

import deim.urv.cat.homework2.model.Model;
import deim.urv.cat.homework2.model.User;
import deim.urv.cat.homework2.service.UserServiceImpl;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@Path("profile")
public class ProfileController {

    @Inject
    Models models;
    @Inject
    HttpSession session;
    @Inject
    UserServiceImpl userService; // Inyectamos la implementación para acceder a métodos específicos
    @Inject
    Logger log;

    @GET
    public String showProfile() {
        User sessionUser = (User) session.getAttribute("user");

        // 1. Verificar sesión
        if (sessionUser == null) {
            return "redirect:/login?returnUrl=profile";
        }

        try {
            // 2. Obtener datos frescos del Backend (incluyendo HATEOAS links)
            User freshUser = userService.findUserByUsername(sessionUser.getUsername(), sessionUser.getPassword());

            if (freshUser != null) {
                models.put("userProfile", freshUser);

                // 3. Procesar enlace HATEOAS ("model")
                if (freshUser.getLinks() != null && freshUser.getLinks().containsKey("model")) {
                    String modelUrl = freshUser.getLinks().get("model");
                    log.log(Level.INFO, "Fetching last viewed model from: {0}", modelUrl);

                    // 4. Invocar la URL del modelo directamente
                    Model lastModel = fetchModelByUrl(modelUrl, sessionUser.getUsername(), sessionUser.getPassword());
                    if (lastModel != null) {
                        models.put("lastModel", lastModel);
                    }
                }
            } else {
                // Fallback si falla la conexión al backend pero hay sesión
                models.put("userProfile", sessionUser);
            }

        } catch (Exception e) {
            log.log(Level.SEVERE, "Error loading profile", e);
            models.put("error", "Could not load profile data.");
        }

        return "profile.jsp";
    }

    /**
     * Método auxiliar para hacer una petición GET a una URL absoluta (HATEOAS)
     */
    private Model fetchModelByUrl(String url, String username, String password) {
        try {
            Client client = ClientBuilder.newClient();

            String authString = username + ":" + password;
            String authHeader = "Basic " + Base64.getEncoder().encodeToString(authString.getBytes("UTF-8"));

            Response response = client.target(url)
                    .request(MediaType.APPLICATION_JSON)
                    .header("Authorization", authHeader)
                    .get();

            if (response.getStatus() == 200) {
                return response.readEntity(Model.class);
            }
        } catch (Exception e) {
            log.log(Level.WARNING, "Error fetching HATEOAS link: " + url, e);
        }
        return null;
    }
}
