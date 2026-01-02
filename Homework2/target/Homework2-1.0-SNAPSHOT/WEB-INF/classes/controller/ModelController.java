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
import java.util.List;

@Path("models")
@Controller
public class ModelController {

    @Inject
    private Models models;
    
    // Instanciamos el servicio. En un entorno CDI completo podrías usar @Inject también aquí.
    private final ModelService service = new ModelServiceImpl();

    @GET
    public String showModels(
            @QueryParam("capability") List<String> capabilities,
            @QueryParam("provider") String provider) {

        // 1. Obtener datos del Backend
        List<Model> list = service.findAll(capabilities, provider);

        // 2. Pasarlos a la vista
        models.put("modelList", list);
        
        // 3. Pasar los filtros actuales para mantenerlos en el formulario
        models.put("selectedProvider", provider);
        // Nota: Pasar la lista de capacidades seleccionadas requiere un poco más de lógica en el JSP,
        // por simplicidad lo omitimos aquí, pero el provider sí se mantendrá.

        // 4. Retornar el nombre de la vista (JSP)
        return "model-list.jsp";
    }
}
