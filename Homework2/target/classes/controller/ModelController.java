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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet; // Importante para ordenar alfabéticamente
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
    public String listModels(@QueryParam("capability") String capability,
            @QueryParam("provider") String provider) {

        // 0. Preparar el filtro de capabilities
        // Recibimos un String del formulario, pero el servicio espera una Lista.
        List<String> capabilities = new ArrayList<>();
        if (capability != null && !capability.trim().isEmpty()) {
            capabilities.add(capability);
        }

        // 1. Obtener la lista filtrada para MOSTRAR los modelos
        List<Model> list = modelService.findAll(capabilities, provider);
        models.put("modelList", list);

        // 2. Obtener TODOS los modelos para generar los desplegables
        List<Model> allModels = modelService.findAll(null, null);

        // Usamos TreeSet para que las opciones aparezcan ordenadas alfabéticamente
        Set<String> uniqueCapabilities = new TreeSet<>();

        for (Model m : allModels) {
            // Añadir la capacidad principal
            if (m.getMainCapability() != null && !m.getMainCapability().isEmpty()) {
                uniqueCapabilities.add(m.getMainCapability());
            }
            // Añadir las capacidades secundarias de la lista
            if (m.getCapabilities() != null) {
                for (String cap : m.getCapabilities()) {
                    if (cap != null && !cap.isEmpty()) {
                        uniqueCapabilities.add(cap);
                    }
                }
            }
        }

        // Pasamos la lista de capacidades ordenadas a la vista
        models.put("allCapabilities", uniqueCapabilities);

        // Hacemos lo mismo para Providers (TreeSet para ordenar)
        Set<String> uniqueProviders = new TreeSet<>();
        for (Model m : allModels) {
            if (m.getProvider() != null && !m.getProvider().isEmpty()) {
                uniqueProviders.add(m.getProvider());
            }
        }
        models.put("allProviders", uniqueProviders);

        return "model-list.jsp";
    }

    @GET
    @Path("{id}")
    public String showDetail(@PathParam("id") Long id) {
        User currentUser = (User) session.getAttribute("user");
        String username = null;
        String password = null;

        if (currentUser != null) {
            username = currentUser.getUsername();
            password = currentUser.getPassword();
        }

        try {
            Model model = modelService.find(id, username, password);

            if (model != null) {
                models.put("model", model);
                if (currentUser != null) {
                    currentUser.setLastConsultedModelId(id);
                    if (userService instanceof UserServiceImpl) {
                        ((UserServiceImpl) userService).updateUser(currentUser, password);
                    }
                }
                return "model-detail.jsp";
            } else {
                return "redirect:/models";
            }

        } catch (NotAuthorizedException e) {
            Logger.getLogger(ModelController.class.getName()).log(Level.INFO, "Acceso no autorizado a modelo privado.");
            return "redirect:/login";
        } catch (Exception e) {
            Logger.getLogger(ModelController.class.getName()).log(Level.SEVERE, "Error en showDetail", e);
            return "redirect:/models";
        }
    }
}
