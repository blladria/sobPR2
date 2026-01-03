/*
 * Archivo: Homework2/src/main/java/deim/urv/cat/homework2/controller/ModelController.java
 * Ubicación: Frontend (Cliente MVC)
 */
package deim.urv.cat.homework2.controller;

import deim.urv.cat.homework2.model.Model;
import deim.urv.cat.homework2.model.User;
import deim.urv.cat.homework2.service.ModelService;
import deim.urv.cat.homework2.service.UserService;
import deim.urv.cat.homework2.service.UserServiceImpl;

import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@Path("models")
public class ModelController {

    @Inject
    Models models;
    @Inject
    ModelService modelService;
    @Inject
    UserService userService;
    @Inject
    HttpSession session;

    @GET
    public String listModels(@QueryParam("capability") List<String> capabilities,
            @QueryParam("provider") String provider) {
        List<Model> list = modelService.findAll(capabilities, provider);
        models.put("modelList", list);
        return "model-list.jsp";
    }

    @GET
    @Path("{id}")
    public String showDetail(@PathParam("id") Long id) {
        // 1. Obtener credenciales si existen en sesión
        User currentUser = (User) session.getAttribute("user");
        String username = null;
        String password = null;

        if (currentUser != null) {
            username = currentUser.getUsername();
            password = currentUser.getPassword();
        }

        try {
            // 2. Intentar buscar el modelo. 
            // Si es privado y no estamos logueados (o credenciales mal), lanzará NotAuthorizedException
            Model model = modelService.find(id, username, password);

            if (model != null) {
                models.put("model", model);

                // 3. ACTUALIZAR 'lastConsultedModelId' SI EL USUARIO ESTÁ LOGUEADO
                if (currentUser != null) {
                    currentUser.setLastConsultedModelId(id);
                    // Actualizamos en backend
                    if (userService instanceof UserServiceImpl) {
                        ((UserServiceImpl) userService).updateUser(currentUser, password);
                    }
                }
                return "model-detail.jsp";
            } else {
                // Si devuelve null (ej. 404 Not Found), volvemos al listado
                return "redirect:/models";
            }

        } catch (NotAuthorizedException e) {
            // REQUISITO CUMPLIDO: Si es privado y no autorizado, redirigir al login
            Logger.getLogger(ModelController.class.getName()).log(Level.INFO, "Acceso no autorizado a modelo privado. Redirigiendo a Login.");
            return "redirect:/login";
        } catch (Exception e) {
            Logger.getLogger(ModelController.class.getName()).log(Level.SEVERE, "Error inesperado en showDetail", e);
            return "redirect:/models";
        }
    }
}
