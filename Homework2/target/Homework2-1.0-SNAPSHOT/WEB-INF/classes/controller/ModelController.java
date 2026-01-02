/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deim.urv.cat.homework2.controller;

import deim.urv.cat.homework2.model.Model;
import deim.urv.cat.homework2.service.ModelService;
import deim.urv.cat.homework2.service.ModelServiceImpl;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
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
    
    // Instanciamos el servicio.
    private final ModelService service = new ModelServiceImpl();

    @GET
    public String showModels(
            @QueryParam("capability") List<String> capabilities,
            @QueryParam("provider") String provider) {

        try {
            // 1. Obtener datos del Backend de forma segura
            List<Model> list = service.findAll(capabilities, provider);
            
            if (list == null) {
                list = new ArrayList<>();
            }

            // 2. Pasarlos a la vista (Verificando que models no sea null por problemas de CDI)
            if (models != null) {
                models.put("modelList", list);
                models.put("selectedProvider", provider);
            } else {
                Logger.getLogger(ModelController.class.getName()).log(Level.SEVERE, "Error: Objeto 'models' es nulo. Fallo de inyección CDI.");
            }
            
        } catch (Exception e) {
            Logger.getLogger(ModelController.class.getName()).log(Level.SEVERE, "Error fatal en ModelController", e);
            // Opcional: Podrías redirigir a una página de error, pero por ahora mostramos la lista vacía
            if (models != null) models.put("modelList", new ArrayList<>());
        }

        // 4. Retornar el nombre de la vista (JSP)
        // Asegúrate que el fichero existe en /WEB-INF/views/model-list.jsp
        return "model-list.jsp";
    }
}