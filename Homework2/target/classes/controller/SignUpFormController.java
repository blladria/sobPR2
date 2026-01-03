package deim.urv.cat.homework2.controller;

import deim.urv.cat.homework2.service.UserService;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.binding.BindingResult;
import jakarta.validation.Valid;
import jakarta.validation.executable.ExecutableType;
import jakarta.validation.executable.ValidateOnExecution;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import java.util.logging.Logger;

@Path("SignUp")
@Controller
public class SignUpFormController {

    @Inject
    private Logger log;

    @Inject
    private UserService service;

    @Inject
    private Models models;

    @Inject
    private BindingResult bindingResult;

    @GET
    public String showForm() {
        return "signup-form.jsp";
    }

    @POST
    @ValidateOnExecution(type = ExecutableType.NONE)
    public String signUp(@Valid @BeanParam UserForm userForm) {
        if (bindingResult.isFailed()) {
            models.put("errors", bindingResult.getAllErrors());
            return "signup-form.jsp";
        }

        // Llamamos al servicio para crear el usuario
        boolean success = service.addUser(userForm);

        if (success) {
            // CAMBIO SOLICITADO: Redirigir al login en lugar de mostrar success.jsp
            // Añadimos ?registered=true por si quieres mostrar un mensaje de éxito en el login
            return "redirect:login?registered=true"; 
        } else {
            // Si falla (ej. usuario ya existe), volvemos al formulario con error
            models.put("formError", "El usuario ya existe o hubo un error.");
            return "signup-form.jsp";
        }
    }
}