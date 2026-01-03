package deim.urv.cat.homework2.controller;

import deim.urv.cat.homework2.model.Model;
import deim.urv.cat.homework2.model.User;
import deim.urv.cat.homework2.service.ModelService;
import deim.urv.cat.homework2.service.ModelServiceImpl;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.UriRef;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
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
    private HttpServletRequest request; // Necesario para la sesión
    
    // Instanciamos el servicio (Mantenemos tu patrón actual)
    private final ModelService service = new ModelServiceImpl();

    @GET
    public String showModels(
            @QueryParam("capability") List<String> capabilities,
            @QueryParam("provider") String provider) {
        // ... (Tu código existente showModels aquí) ...
        // [Mantenlo igual que en tu fichero original]
        try {
            List<Model> list = service.findAll(capabilities, provider);
            if (list == null) list = new ArrayList<>();
            if (models != null) {
                models.put("modelList", list);
                models.put("selectedProvider", provider);
            }
        } catch (Exception e) {
            Logger.getLogger(ModelController.class.getName()).log(Level.SEVERE, "Error en ModelController", e);
            if (models != null) models.put("modelList", new ArrayList<>());
        }
        return "model-list.jsp";
    }

    // --- NUEVO MÉTODO PARA EL DETALLE ---
    @GET
    @Path("detail")
    @UriRef("detail")
    public String showDetail(@QueryParam("id") Long id) {
        try {
            // 1. Recuperar credenciales de la sesión si existen
            HttpSession session = request.getSession(false);
            String username = null;
            String password = null;

            if (session != null && session.getAttribute("user") != null) {
                User user = (User) session.getAttribute("user");
                username = user.getUsername();
                password = user.getPassword(); // Necesitamos la password en texto plano para Basic Auth
            }

            // 2. Llamar al servicio pasando credenciales
            Model model = service.find(id, username, password);

            // 3. Pasar el modelo a la vista
            if (model != null) {
                models.put("model", model);
                return "model-detail.jsp";
            } else {
                // Si es privado y no hay login, o no existe, el servicio devuelve null
                models.put("error", "Model not found or access denied.");
                return "model-list.jsp"; // O una página de error
            }
        } catch (Exception e) {
            Logger.getLogger(ModelController.class.getName()).log(Level.SEVERE, "Error retrieving model detail", e);
            return "error.jsp"; // Asegúrate de tener una página de error o redirigir
        }
    }
}