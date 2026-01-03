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
import jakarta.validation.Valid; // Importación necesaria para la validación
import jakarta.ws.rs.BeanParam; // Importación necesaria para usar LoginForm
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
    public String login(@Valid @BeanParam LoginForm form) {
        // USO DE LoginForm: Ahora recibimos todos los datos agrupados y validados.

        // 1. Comprobar errores de validación (Campos vacíos, etc.)
        if (bindingResult.isFailed()) {
            // Obtenemos el primer mensaje de error para mostrarlo en la alerta del JSP
            String errorMessage = bindingResult.getAllErrors().stream()
                                    .map(e -> e.getMessage())
                                    .findFirst()
                                    .orElse("Error en los datos del formulario.");
            
            models.put("error", errorMessage);
            models.put("username", form.getUsername()); // Mantener el usuario escrito
            
            if (form.getReturnUrl() != null && !form.getReturnUrl().isEmpty()) {
                models.put("returnUrl", form.getReturnUrl());
            }
            return "login.jsp";
        }

        // 2. Intentar validar credenciales con el servicio
        User user = service.validateUser(form.getUsername(), form.getPassword());

        if (user != null) {
            // LOGIN CORRECTO

            // Guardamos el usuario en la sesión actual
            HttpSession session = request.getSession(true);
            session.setAttribute("user", user);

            // Redirección inteligente
            String returnUrl = form.getReturnUrl();
            if (returnUrl != null && !returnUrl.trim().isEmpty() && returnUrl.startsWith("/")) {
                return "redirect:" + returnUrl;
            }

            // Redirección por defecto a la lista de modelos
            return "redirect:models";

        } else {
            // LOGIN FALLIDO (Credenciales incorrectas)
            models.put("error", "Usuario o contraseña incorrectos.");
            models.put("username", form.getUsername());

            if (form.getReturnUrl() != null && !form.getReturnUrl().isEmpty()) {
                models.put("returnUrl", form.getReturnUrl());
            }
            return "login.jsp";
        }
    }
}