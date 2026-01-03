/*
 * Archivo: Homework2/src/main/java/deim/urv/cat/homework2/controller/ModelController.java
 * Ubicación: Frontend (Cliente MVC)
 */
package deim.urv.cat.homework2.controller;

import deim.urv.cat.homework2.model.Model;
import deim.urv.cat.homework2.model.User;
import deim.urv.cat.homework2.service.ModelService;
import deim.urv.cat.homework2.service.UserService; // Necesario castear a Impl si no está en interfaz
import deim.urv.cat.homework2.service.UserServiceImpl;

import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import java.util.List;

@Controller
@Path("models")
public class ModelController {

    @Inject
    Models models;
    @Inject
    ModelService modelService;
    @Inject
    UserService userService; // Inyectamos servicio de usuario
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
        // 1. Obtener credenciales si existen
        User currentUser = (User) session.getAttribute("user");
        String username = null;
        String password = null;

        if (currentUser != null) {
            username = currentUser.getUsername();
            password = currentUser.getPassword();
        }

        // 2. Buscar el modelo
        Model model = modelService.find(id, username, password);

        if (model != null) {
            models.put("model", model);

            // 3. ACTUALIZAR 'lastConsultedModelId' SI EL USUARIO ESTÁ LOGUEADO
            if (currentUser != null) {
                currentUser.setLastConsultedModelId(id);
                // Llamamos al servicio para hacer el PUT al backend
                // Hacemos cast porque añadimos el método en la implementación (idealmente añadir a la interfaz)
                if (userService instanceof UserServiceImpl) {
                    ((UserServiceImpl) userService).updateUser(currentUser, password);
                }
            }
            return "model-detail.jsp";
        } else {
            return "redirect:/models";
        }
    }
}
