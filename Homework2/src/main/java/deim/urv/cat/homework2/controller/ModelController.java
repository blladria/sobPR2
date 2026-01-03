package deim.urv.cat.homework2.controller;

import deim.urv.cat.homework2.model.Model;
import deim.urv.cat.homework2.model.User;
import deim.urv.cat.homework2.service.ModelService;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.UriRef;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("models")
@Controller
public class ModelController {

    @Inject
    private Models models;
    
    @Inject
    private HttpServletRequest request;
    
    // Inyección del servicio (CDI) para usar la implementación que conecta a la API
    @Inject
    private ModelService service;

    @GET
    public String showModels(
            @QueryParam("capability") String capability,
            @QueryParam("provider") String provider) {
        try {
            // 1. Preparar filtro
            List<String> capabilitiesFilter = new ArrayList<>();
            if (capability != null && !capability.isEmpty()) {
                capabilitiesFilter.add(capability);
            }

            // 2. Llamar al servicio (que llama a la API)
            List<Model> list = service.findAll(capabilitiesFilter, provider);
            if (list == null) list = new ArrayList<>();
            
            // 3. Obtener lista de capabilities para el desplegable
            List<String> allCapabilities = service.getUniqueCapabilities();

            // 4. Pasar datos a la vista
            if (models != null) {
                // CORRECCIÓN: Usamos "models" porque en el JSP tienes items="${models}"
                models.put("models", list); 
                models.put("capabilitiesList", allCapabilities);
                models.put("selectedProvider", provider);
                models.put("selectedCapability", capability);
            }
        } catch (Exception e) {
            Logger.getLogger(ModelController.class.getName()).log(Level.SEVERE, "Error en ModelController", e);
            if (models != null) models.put("models", new ArrayList<>());
        }
        return "model-list.jsp";
    }

    @GET
    @Path("detail")
    @UriRef("detail")
    public String showDetail(@QueryParam("id") Long id) {
        try {
            HttpSession session = request.getSession(false);
            String username = null;
            String password = null;
            boolean isLoggedIn = false;

            // Verificar si el usuario está logueado en la sesión web
            if (session != null && session.getAttribute("user") != null) {
                User user = (User) session.getAttribute("user");
                username = user.getUsername();
                password = user.getPassword(); 
                isLoggedIn = true;
            }

            // Buscar modelo (si es privado, el servicio usará las credenciales)
            Model model = service.find(id, username, password);

            if (model != null) {
                models.put("model", model);
                return "model-detail.jsp";
            } else {
                // Si falla y no estamos logueados, redirigir a login
                if (!isLoggedIn) {
                    String returnUrl = "models/detail?id=" + id;
                    String encodedUrl = URLEncoder.encode(returnUrl, StandardCharsets.UTF_8.toString());
                    return "redirect:/login?returnUrl=" + encodedUrl;
                }
                models.put("error", "Model not found or access denied.");
                return "model-list.jsp"; 
            }
        } catch (Exception e) {
            Logger.getLogger(ModelController.class.getName()).log(Level.SEVERE, "Error recuperando detalle", e);
            return "error.jsp"; 
        }
    }
}