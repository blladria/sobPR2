package deim.urv.cat.homework2.controller;

import deim.urv.cat.homework2.model.User;
import deim.urv.cat.homework2.service.UserService;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.mvc.binding.BindingResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import java.util.logging.Logger;

@Controller
@Path("login")
public class LoginController {

    @Inject
    UserService service;

    @Inject
    Models models;

    @Inject
    BindingResult bindingResult;

    @Inject
    Logger log;

    @Context
    HttpServletRequest request; // Necesario para la sesión

    @GET
    @View("login.jsp")
    public void showLoginForm(@QueryParam("returnUrl") String returnUrl) {
        if (returnUrl != null && !returnUrl.isEmpty()) {
            models.put("returnUrl", returnUrl);
        }
    }

    @POST
    public String login(@FormParam("username") String username,
            @FormParam("password") String password,
            @FormParam("returnUrl") String returnUrl) {

        // CORRECCIÓN: Usamos validateUser que devuelve un User o null
        User user = service.validateUser(username, password);

        if (user != null) {
            // LOGIN CORRECTO

            // 1. Guardamos el usuario en la sesión actual
            HttpSession session = request.getSession(true);
            session.setAttribute("user", user); // 'user' es el objeto que devuelve validateUser

            // 2. Redirección inteligente
            if (returnUrl != null && !returnUrl.trim().isEmpty() && returnUrl.startsWith("/")) {
                return "redirect:" + returnUrl;
            }
            return "redirect:/models";

        } else {
            // LOGIN FALLIDO
            models.put("error", "Usuario o contraseña incorrectos.");
            models.put("username", username);

            if (returnUrl != null && !returnUrl.isEmpty()) {
                models.put("returnUrl", returnUrl);
            }
            return "login.jsp";
        }
    }
}
