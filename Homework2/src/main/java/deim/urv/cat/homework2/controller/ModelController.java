/*
 * Archivo: Homework2/src/main/java/deim/urv/cat/homework2/controller/ModelController.java
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

        // 1. Obtener SIEMPRE todos los modelos del backend
        List<Model> allModels = modelService.findAll(null, null);
        
        List<Model> filteredList = new ArrayList<>();
        Set<String> uniqueCapabilities = new TreeSet<>();
        Set<String> uniqueProviders = new TreeSet<>();

        // 2. Procesar filtros y limpiar datos
        for (Model m : allModels) {
            
            // Generar opciones para desplegables
            if (m.getProvider() != null && !m.getProvider().isEmpty()) {
                uniqueProviders.add(m.getProvider());
            }

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

            // Aplicar Filtros (Frontend Filtering)
            boolean matchesProvider = (provider == null || provider.trim().isEmpty()) || 
                                      (m.getProvider() != null && m.getProvider().equals(provider));

            boolean matchesCapability = (capability == null || capability.trim().isEmpty());
            
            if (!matchesCapability) {
                if (m.getMainCapability() != null && formatText(m.getMainCapability()).equals(capability)) {
                    matchesCapability = true;
                }
                else if (m.getCapabilities() != null) {
                    for (String cap : m.getCapabilities()) {
                        if (formatText(cap).equals(capability)) {
                            matchesCapability = true;
                            break;
                        }
                    }
                }
            }

            if (matchesProvider && matchesCapability) {
                filteredList.add(m);
            }
        }

        // --- ORDENACIÓN Z-A (Requisito) ---
        Collections.sort(filteredList, new Comparator<Model>() {
            @Override
            public int compare(Model m1, Model m2) {
                String n1 = m1.getName();
                String n2 = m2.getName();
                if (n1 == null) n1 = "";
                if (n2 == null) n2 = "";
                return n2.compareToIgnoreCase(n1); // Orden Inverso
            }
        });

        // 3. Pasar los datos a la vista
        models.put("modelList", filteredList);
        models.put("allCapabilities", uniqueCapabilities);
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
            return "redirect:/login?returnUrl=models/" + id;
        } catch (Exception e) {
            Logger.getLogger(ModelController.class.getName()).log(Level.SEVERE, "Error en showDetail", e);
            return "redirect:/models";
        }
    }

    private String formatText(String input) {
        if (input == null) return "";
        String text = input.replace("-", " ");
        StringBuilder result = new StringBuilder();
        String[] words = text.split("\\s+");
        for (String word : words) {
            if (!word.isEmpty()) {
                if (result.length() > 0) result.append(" ");
                result.append(Character.toUpperCase(word.charAt(0)))
                      .append(word.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }
}