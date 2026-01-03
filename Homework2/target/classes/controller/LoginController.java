/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deim.urv.cat.homework2.controller;

import deim.urv.cat.homework2.model.User;
import deim.urv.cat.homework2.service.UserService;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.binding.BindingResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import java.util.logging.Logger;

@Path("login")
@Controller
public class LoginController {

    @Inject Logger log;
    @Inject UserService userService;
    @Inject Models models;
    @Inject BindingResult bindingResult;
    @Inject HttpServletRequest request;

    // CAMBIO: Recibimos returnUrl por GET
    @GET
    public String showLogin(@QueryParam("returnUrl") String returnUrl) {
        if (returnUrl != null && !returnUrl.isEmpty()) {
            models.put("returnUrl", returnUrl);
        }
        return "login.jsp";
    }

    @POST
    public String login(@Valid @BeanParam LoginForm form) {
        // 1. Si hay errores de validación (campos vacíos)
        if (bindingResult.isFailed()) {
            models.put("errors", bindingResult.getAllErrors());
            models.put("loginForm", form); // Devolvemos lo que escribió el usuario
            return "login.jsp";
        }

        // 2. Validar credenciales contra el backend
        User user = userService.validateUser(form.getUsername(), form.getPassword());

        if (user != null) {
            // LOGIN CORRECTO
            HttpSession session = request.getSession(true);
            user.setPassword(form.getPassword()); 
            session.setAttribute("user", user);
            
            // CAMBIO: Redirección Inteligente
            if (form.getReturnUrl() != null && !form.getReturnUrl().trim().isEmpty()) {
                // Redirigimos a la página que intentaba visitar
                // Nota: Asumimos que returnUrl es una ruta relativa válida dentro de /Web/
                return "redirect:/" + form.getReturnUrl();
            }
            
            // Default
            return "redirect:/models";
        } else {
            // LOGIN FALLIDO (Usuario no existe o password mal)
            models.put("loginError", "Invalid username or password.");
            models.put("loginForm", form); // Devolvemos el usuario
            return "login.jsp";
        }
    }
}