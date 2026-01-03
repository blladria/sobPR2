/*
 * Archivo: Homework2/src/main/java/deim/urv/cat/homework2/controller/LogoutController.java
 * Ubicación: Frontend (Cliente MVC)
 */
package deim.urv.cat.homework2.controller;

import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import java.util.logging.Logger;

@Path("logout")
@Controller
public class LogoutController {

    @Inject
    HttpServletRequest request;

    @Inject
    Logger log;

    @GET
    public String logout() {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // Destruye la sesión actual
            log.info("User session invalidated successfully.");
        }
        // Redirige a la página principal (o login) tras salir
        return "redirect:/models";
    }
}
