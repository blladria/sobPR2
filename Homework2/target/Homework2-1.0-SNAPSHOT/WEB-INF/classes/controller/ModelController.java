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
            // CAMBIO: Añadido /Web para que la redirección encuentre la ruta correcta
            return "redirect:/Web/models";
        }

        // Obtenemos usuario de sesión para intentar acceder con credenciales si las hay
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        String username = (user != null) ? user.getUsername() : null;
        String password = (user != null) ? user.getPassword() : null;

        try {
            // CORRECCIÓN ANTERIOR: El servicio usa 'find' con 3 argumentos
            Model model = modelService.find(id, username, password);

            if (model == null) {
                // CAMBIO: Añadido /Web
                return "redirect:/Web/models";
            }

            // CORRECCIÓN ANTERIOR: El getter es isPrivate()
            if (model.isPrivate()) {

                // Si es privado y NO estamos logueados, redirigimos al login
                if (user == null) {
                    // CAMBIO CRÍTICO: Se añade "/Web" a la URL de retorno.
                    // Sin esto, al hacer login exitoso, redirige a una página 404.
                    String currentUrl = "/Web/models/detail?id=" + id;

                    // 'returnUrl' debe coincidir con lo que espera LoginController
                    return "redirect:/login?returnUrl=" + currentUrl;
                }
            }

            models.put("model", model);
            return "model-detail.jsp";

        } catch (Exception e) {
            // Si el servicio falla (por ejemplo, 403 Forbidden)
            // asumimos que hace falta login y redirigimos.

            // CAMBIO CRÍTICO: Se añade "/Web" aquí también
            String currentUrl = "/Web/models/detail?id=" + id;
            return "redirect:/login?returnUrl=" + currentUrl;
        }
    }
}
