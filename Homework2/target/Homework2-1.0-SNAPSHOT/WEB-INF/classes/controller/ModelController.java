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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
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

        // 1. Obtener SIEMPRE todos los modelos del backend (sin filtrar allí)
        List<Model> allModels = modelService.findAll(null, null);

        // Listas para almacenar los resultados filtrados y las opciones de los desplegables
        List<Model> filteredList = new ArrayList<>();
        Set<String> uniqueCapabilities = new TreeSet<>();
        Set<String> uniqueProviders = new TreeSet<>();

        // 2. Recorremos TODOS los modelos una sola vez
        for (Model m : allModels) {

            // A) Generar las opciones para los desplegables (Providers)
            if (m.getProvider() != null && !m.getProvider().isEmpty()) {
                uniqueProviders.add(m.getProvider());
            }

            // B) Generar las opciones para los desplegables (Capabilities normalizadas)
            if (m.getMainCapability() != null && !m.getMainCapability().isEmpty()) {
                uniqueCapabilities.add(formatText(m.getMainCapability()));
            }
            if (m.getCapabilities() != null) {
                for (String cap : m.getCapabilities()) {
                    if (cap != null && !cap.isEmpty()) {
                        uniqueCapabilities.add(formatText(cap));
                    }
                }
            }

            // C) LÓGICA DE FILTRADO
            // Comprobamos si el modelo cumple con el Provider seleccionado
            boolean matchesProvider = (provider == null || provider.trim().isEmpty())
                    || (m.getProvider() != null && m.getProvider().equals(provider));

            // Comprobamos si el modelo cumple con la Capability seleccionada
            boolean matchesCapability = (capability == null || capability.trim().isEmpty());

            if (!matchesCapability) { // Si hay algo seleccionado en el filtro...
                // Verificar Main Capability
                if (m.getMainCapability() != null && formatText(m.getMainCapability()).equals(capability)) {
                    matchesCapability = true;
                } // Verificar lista de Capabilities
                else if (m.getCapabilities() != null) {
                    for (String cap : m.getCapabilities()) {
                        if (formatText(cap).equals(capability)) {
                            matchesCapability = true;
                            break;
                        }
                    }
                }
            }

            // Si cumple AMBOS filtros, lo añadimos a la lista que se mostrará
            if (matchesProvider && matchesCapability) {
                filteredList.add(m);
            }
        }

        // --- IMPLEMENTACIÓN NUEVA: ORDENACIÓN DESCENDENTE (Z-A) ---
        // Requisito 2.1: "ordenats alfabèticament de forma descendent"
        Collections.sort(filteredList, new Comparator<Model>() {
            @Override
            public int compare(Model m1, Model m2) {
                String n1 = (m1.getName() != null) ? m1.getName() : "";
                String n2 = (m2.getName() != null) ? m2.getName() : "";
                // m2 compareTo m1 para orden descendente
                return n2.compareToIgnoreCase(n1);
            }
        });

        // 3. Pasar los datos a la vista
        models.put("modelList", filteredList); // La lista filtrada y ordenada
        models.put("allCapabilities", uniqueCapabilities); // Opciones del desplegable
        models.put("allProviders", uniqueProviders); // Opciones del desplegable

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
                    // Actualizamos el usuario en backend para persistir el "Last Consulted"
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
            return "redirect:/login?returnUrl=models/" + id;
        } catch (Exception e) {
            Logger.getLogger(ModelController.class.getName()).log(Level.SEVERE, "Error en showDetail", e);
            return "redirect:/models";
        }
    }

    /**
     * Método auxiliar para limpiar y estandarizar los textos. Convierte
     * "code-generation" -> "Code Generation".
     */
    private String formatText(String input) {
        if (input == null) {
            return "";
        }

        // 1. Reemplazar guiones por espacios
        String text = input.replace("-", " ");

        // 2. Convertir a "Title Case"
        StringBuilder result = new StringBuilder();
        String[] words = text.split("\\s+");

        for (String word : words) {
            if (!word.isEmpty()) {
                if (result.length() > 0) {
                    result.append(" ");
                }
                result.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase());
            }
        }

        return result.toString();
    }
}
