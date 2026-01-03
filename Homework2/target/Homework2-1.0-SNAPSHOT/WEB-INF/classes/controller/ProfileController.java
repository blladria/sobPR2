package deim.urv.cat.homework2.controller;

import deim.urv.cat.homework2.model.User;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("profile")
@Controller
public class ProfileController {

    @Inject
    private Models models;

    @Inject
    private HttpServletRequest request;

    @GET
    public String showProfile() {
        HttpSession session = request.getSession(false);
        
        // 1. Verificar si hay usuario en sesión
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            models.put("user", user);
            return "profile.jsp";
        } else {
            // Si no está logueado, al login
            return "redirect:login";
        }
    }
}