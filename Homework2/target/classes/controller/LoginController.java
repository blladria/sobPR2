package deim.urv.cat.homework2.controller;

import deim.urv.cat.homework2.model.User;
import deim.urv.cat.homework2.service.UserService;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.binding.BindingResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("login")
@Controller
public class LoginController {

    @Inject
    private Models models;
    
    @Inject
    private UserService service;
    
    @Inject
    private BindingResult bindingResult;
    
    @Inject
    private HttpServletRequest request;

    @GET
    public String showLogin(@QueryParam("returnUrl") String returnUrl) {
        // Pasamos el returnUrl a la vista para que el formulario lo mantenga
        models.put("returnUrl", returnUrl);
        return "login.jsp";
    }

    @POST
    public String login(@BeanParam LoginForm form) {
        if (bindingResult.isFailed()) {
            models.put("errors", bindingResult.getAllErrors());
            return "login.jsp";
        }

        // Validar credenciales contra el servicio REST
        User user = service.validateUser(form.getUsername(), form.getPassword());

        if (user != null) {
            // Guardar usuario en sesión
            HttpSession session = request.getSession(true);
            session.setAttribute("user", user);
            
            // Lógica de redirección solicitada en el enunciado
            String returnUrl = form.getReturnUrl();
            if (returnUrl != null && !returnUrl.isEmpty() && !returnUrl.equals("null")) {
                // Redirigir a la página donde estaba el usuario (ej: detalle de modelo privado)
                return "redirect:/" + returnUrl;
            } else {
                // Si no venía de ningún lado, ir al inicio
                return "redirect:models";
            }
        } else {
            models.put("loginError", "Usuario o contraseña incorrectos.");
            // Mantener el returnUrl por si quiere reintentar
            models.put("returnUrl", form.getReturnUrl());
            return "login.jsp";
        }
    }
}