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
import java.util.logging.Logger;

@Path("login")
@Controller
public class LoginController {

    @Inject Logger log;
    @Inject UserService userService;
    @Inject Models models;
    @Inject BindingResult bindingResult;
    @Inject HttpServletRequest request;

    @GET
    public String showLogin() {
        return "login.jsp";
    }

    @POST
    public String login(@Valid @BeanParam LoginForm form) {
        if (bindingResult.isFailed()) {
            models.put("errors", bindingResult.getAllErrors());
            return "login.jsp";
        }

        // Validar credenciales contra el backend
        User user = userService.validateUser(form.getUsername(), form.getPassword());

        if (user != null) {
            // LOGIN CORRECTO
            HttpSession session = request.getSession(true);
            
            // IMPORTANTE: Guardamos la password en el objeto User de sesión
            // para poder usarla en ModelService (Basic Auth) más tarde.
            user.setPassword(form.getPassword()); 
            
            session.setAttribute("user", user);
            log.info("Usuario logueado: " + user.getUsername());
            
            return "redirect:/models";
        } else {
            // LOGIN FALLIDO
            models.put("loginError", "Invalid username or password.");
            return "login.jsp";
        }
    }
}