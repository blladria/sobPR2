package deim.urv.cat.homework2.controller;

import deim.urv.cat.homework2.model.AlertMessage;
import deim.urv.cat.homework2.model.SignUpAttempts;
import deim.urv.cat.homework2.service.UserService;
import deim.urv.cat.homework2.model.User;

import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.UriRef;
import jakarta.mvc.binding.BindingResult;
import jakarta.mvc.binding.ParamError;
import jakarta.mvc.security.CsrfProtected;
import jakarta.validation.Valid;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@Path("SignUp")
public class SignUpFormController {    
    // CDI
    @Inject BindingResult bindingResult;
    @Inject Logger log;
    @Inject UserService service;
    @Inject Models models;
    @Inject AlertMessage flashMessage;
    @Inject SignUpAttempts attempts;
    
    @GET
    public String showForm() {
        return "signup-form.jsp"; // Injects CRSF token
    }    
    
    @POST
    @UriRef("sign-up")
    @CsrfProtected
    public String signUp(@Valid @BeanParam UserForm userForm) {
        models.put("user", userForm);
        
        // 1. Validaciones del formulario (campos vacíos, longitud, etc.)
        if (bindingResult.isFailed()) {
            AlertMessage alert = AlertMessage.danger("Validation failed!");
            bindingResult.getAllErrors()
                    .stream()
                    .forEach((ParamError t) -> {
                        alert.addError(t.getParamName(), "", t.getMessage());
                    });
            log.log(Level.WARNING, "Data binding for signupFormController failed.");
            models.put("errors", alert);
            return "signup-form.jsp";
        }
        
        // 2. Comprobar intentos máximos
        if(attempts.hasExceededMaxAttempts()) {
            return "signup-form.jsp";
        }
       
        // 3. Intento de registro
        log.log(Level.INFO, "Attempting to register user: " + userForm.getUsername());
        
        // CORRECCIÓN: Capturamos si el backend aceptó o rechazó el registro
        boolean success = service.addUser(userForm);
        
        if (success) {
            log.log(Level.INFO, "User registered successfully. Redirecting to success page.");
            attempts.reset();
            return "signup-success.jsp";
        } else {
            // Si devuelve false, es probable que el Username ya exista (409 Conflict)
            log.log(Level.WARNING, "Registration failed (Backend returned non-201 status). Username likely taken.");
            models.put("message", "Registration failed! The username might already be taken.");
            attempts.increment();
            return "signup-form.jsp";
        }
    } 
}