package deim.urv.cat.homework2.controller;

import deim.urv.cat.homework2.service.UserService;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.binding.BindingResult;
import jakarta.validation.Valid;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("SignUp")
@Controller
public class SignUpFormController {

    @Inject
    private Models models;

    @Inject
    private BindingResult bindingResult;

    @Inject
    private UserService service;

    @Inject
    private Logger log;

    @GET
    public String showForm() {
        return "signup-form.jsp";
    }

    @POST
    public String register(@Valid @BeanParam UserForm userForm) {
        // 1. Comprobar errores de validación (Email mal formado, pass corto, etc.)
        if (bindingResult.isFailed()) {
            models.put("errors", bindingResult.getAllErrors());
            models.put("userForm", userForm); // Mantener datos
            return "signup-form.jsp"; // Volver al formulario mostrando errores
        }

        try {
            // 2. Intentar guardar en BD
            boolean success = service.addUser(userForm);

            if (success) {
                // Éxito: Redirigir a Login o Success
                return "redirect:/login";
            } else {
                // Fallo del servicio (Usuario ya existe, error BD)
                models.put("globalError", "Registration failed. User or Email might already exist.");
                models.put("userForm", userForm);
                return "signup-form.jsp";
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error in SignUp", e);
            models.put("globalError", "System error during registration.");
            return "signup-form.jsp";
        }
    }
}
