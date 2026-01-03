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

    @Inject
    BindingResult bindingResult;
    @Inject
    Logger log;
    @Inject
    UserService service;
    @Inject
    Models models;
    @Inject
    AlertMessage flashMessage;
    @Inject
    SignUpAttempts attempts;

    @GET
    public String showForm() {
        // Forzamos que el objeto user en el modelo sea una instancia nueva y vacía
        models.put("user", new UserForm());
        return "signup-form.jsp";
    }

    @POST
    @UriRef("sign-up")
    @CsrfProtected
    public String signUp(@Valid @BeanParam UserForm userForm) {
        models.put("user", userForm);

        if (bindingResult.isFailed()) {
            AlertMessage alert = AlertMessage.danger("Validation failed!");
            bindingResult.getAllErrors()
                    .stream()
                    .forEach((ParamError t) -> {
                        alert.addError(t.getParamName(), "", t.getMessage());
                    });
            models.put("errors", alert);
            return "signup-form.jsp";
        }

        if (attempts.hasExceededMaxAttempts()) {
            return "signup-form.jsp";
        }

        boolean success = service.addUser(userForm);

        if (success) {
            attempts.reset();
            return "signup-success.jsp";
        } else {
            models.put("message", "Registration failed! The username might already be taken.");
            attempts.increment();
            return "signup-form.jsp";
        }
    }
}
