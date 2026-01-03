package deim.urv.cat.homework2.controller;

import deim.urv.cat.homework2.model.Model;
import deim.urv.cat.homework2.model.User;
import deim.urv.cat.homework2.service.ModelService;
import deim.urv.cat.homework2.service.UserService;
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
    public String listModels(@QueryParam("capability") String capability, @QueryParam("provider") String provider) {
        List<Model> allModels = modelService.findAll(null, null);
        List<Model> filteredList = new ArrayList<>();
        Set<String> uniqueCapabilities = new TreeSet<>();
        Set<String> uniqueProviders = new TreeSet<>();

        for (Model m : allModels) {
            if (m.getProvider() != null) {
                uniqueProviders.add(m.getProvider());
            }
            if (m.getMainCapability() != null) {
                uniqueCapabilities.add(m.getMainCapability());
            }

            boolean matchesProvider = (provider == null || provider.isEmpty()) || m.getProvider().equals(provider);
            boolean matchesCapability = (capability == null || capability.isEmpty()) || m.getMainCapability().equals(capability);

            if (matchesProvider && matchesCapability) {
                filteredList.add(m);
            }
        }

        // Ordenación descendente
        Collections.sort(filteredList, (m1, m2) -> m2.getName().compareToIgnoreCase(m1.getName()));

        models.put("modelList", filteredList);
        models.put("allCapabilities", uniqueCapabilities);
        models.put("allProviders", uniqueProviders);
        return "model-list.jsp";
    }

    @GET
    @Path("{id}")
    public String showDetail(@PathParam("id") Long id) {
        User currentUser = (User) session.getAttribute("user");
        String username = (currentUser != null) ? currentUser.getUsername() : null;
        String password = (currentUser != null) ? currentUser.getPassword() : null;

        try {
            Model model = modelService.find(id, username, password);
            if (model != null) {
                models.put("model", model);
                if (currentUser != null) {
                    currentUser.setLastConsultedModelId(id);
                    // LLAMADA CORREGIDA AL SERVICIO
                    userService.updateUser(currentUser, password);
                }
                return "model-detail.jsp";
            }
            return "redirect:/models";
        } catch (NotAuthorizedException e) {
            return "redirect:/login?returnUrl=models/" + id;
        } catch (Exception e) {
            return "redirect:/models";
        }
    }
}
