package deim.urv.cat.homework2.controller;

import deim.urv.cat.homework2.model.Model;
import deim.urv.cat.homework2.model.User; // Asegúrate de importar tu clase User
import deim.urv.cat.homework2.service.ModelService;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Controller
@Path("models")
public class ModelController {

    @Inject
    ModelService modelService;

    @Inject
    Models models;

    @Context
    HttpServletRequest request;

    @GET
    @View("model-list.jsp")
    public void showModels(@QueryParam("capability") String capability,
            @QueryParam("provider") String provider) {

        // Convertir String único a Lista para el servicio
        List<String> capabilities = (capability != null && !capability.isEmpty())
                ? Arrays.asList(capability)
                : Collections.emptyList();

        // 1. Cargar modelos
        models.put("models", modelService.findAll(capabilities, provider));

        // 2. Cargar datos para los filtros (IMPORTANTE para que no falle el JSP)
        models.put("uniqueCapabilities", modelService.getUniqueCapabilities());
        models.put("uniqueProviders", modelService.getUniqueProviders());
    }

    @GET
    @Path("detail")
    public String showModelDetail(@QueryParam("id") Long id) {
        if (id == null) {
            return "redirect:/models";
        }

        // Obtenemos usuario de sesión para intentar acceder con credenciales si las hay
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        String username = (user != null) ? user.getUsername() : null;
        String password = (user != null) ? user.getPassword() : null;

        try {
            // CORRECCIÓN ERROR 2: El servicio usa 'find' con 3 argumentos
            Model model = modelService.find(id, username, password);

            if (model == null) {
                return "redirect:/models";
            }

            // CORRECCIÓN ERROR 1: El getter es isPrivate()
            if (model.isPrivate()) {

                // Si es privado y NO estamos logueados, redirigimos al login
                if (user == null) {
                    String currentUrl = "/models/detail?id=" + id;
                    // 'returnUrl' debe coincidir con lo que espera LoginController
                    return "redirect:/login?returnUrl=" + currentUrl;
                }
            }

            models.put("model", model);
            return "model-detail.jsp";

        } catch (Exception e) {
            // Si el servicio falla (por ejemplo, 403 Forbidden porque no pasamos credenciales)
            // asumimos que hace falta login y redirigimos.
            String currentUrl = "/models/detail?id=" + id;
            return "redirect:/login?returnUrl=" + currentUrl;
        }
    }
}
