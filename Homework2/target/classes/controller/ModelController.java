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
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList; // Por si acaso

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

        // 1. Obtener la lista filtrada para MOSTRAR los modelos (lógica actual)
        List<Model> list = modelService.findAll(capabilities, provider);
        models.put("modelList", list);

        // 2. NUEVO: Obtener TODOS los modelos para generar el FILTRO
        // Llamamos a findAll(null, null) para traer todo sin filtrar
        List<Model> allModels = modelService.findAll(null, null);

        // Usamos un Set para almacenar capacidades únicas (evita duplicados)
        Set<String> uniqueCapabilities = new HashSet<>();

        for (Model m : allModels) {
            // Añadir la capacidad principal
            if (m.getMainCapability() != null && !m.getMainCapability().isEmpty()) {
                uniqueCapabilities.add(m.getMainCapability());
            }
            // Opcional: Si quieres que también aparezcan las capacidades de la lista interna
            if (m.getCapabilities() != null) {
                for (String cap : m.getCapabilities()) {
                    if (cap != null && !cap.isEmpty()) {
                        uniqueCapabilities.add(cap);
                    }
                }
            }
        }

        // Pasamos la lista de capacidades únicas a la vista
        models.put("allCapabilities", uniqueCapabilities);

        // (Opcional) Hacer lo mismo para "Providers" si quisieras que también fuera dinámico
        Set<String> uniqueProviders = new HashSet<>();
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
