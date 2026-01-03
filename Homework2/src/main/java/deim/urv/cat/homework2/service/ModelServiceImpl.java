/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deim.urv.cat.homework2.service;

import deim.urv.cat.homework2.model.Model;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModelServiceImpl implements ModelService {

    private Client client;
    private WebTarget webTarget;
    
    // Asegúrate de que este puerto (8080) y nombre de proyecto (PR1_sob_grup_54) son correctos
    private static final String BASE_URI = "http://localhost:8080/PR1_sob_grup_54/rest/api/v1"; 

    public ModelServiceImpl() {
        try {
            client = ClientBuilder.newClient();
            webTarget = client.target(BASE_URI).path("models");
        } catch (Exception e) {
            Logger.getLogger(ModelServiceImpl.class.getName()).log(Level.SEVERE, "Error iniciando JAX-RS Client", e);
        }
    }

    @Override
    public List<Model> findAll(List<String> capabilities, String provider) {
        // Protección extra si el constructor falló
        if (webTarget == null) return new ArrayList<>();

        try {
            WebTarget target = webTarget;

            // 1. Aplicar Filtro Provider
            if (provider != null && !provider.isEmpty()) {
                target = target.queryParam("provider", provider);
            }

            // 2. Aplicar Filtro Capabilities
            if (capabilities != null && !capabilities.isEmpty()) {
                for (String cap : capabilities) {
                    target = target.queryParam("capability", cap);
                }
            }

            // 3. Hacer la petición GET
            Response response = target.request(MediaType.APPLICATION_JSON).get();

            if (response.getStatus() == 200) {
                // Mapear JSON a Lista de Objetos Model
                return response.readEntity(new GenericType<List<Model>>() {});
            } else {
                Logger.getLogger(ModelServiceImpl.class.getName()).log(Level.WARNING, "Backend respondió con error: {0}", response.getStatus());
                return new ArrayList<>();
            }

        } catch (ProcessingException e) {
            Logger.getLogger(ModelServiceImpl.class.getName()).log(Level.SEVERE, "Error procesando JSON o conectando al backend. Verifica que el backend está corriendo.", e);
            return new ArrayList<>();
        } catch (Exception e) {
            Logger.getLogger(ModelServiceImpl.class.getName()).log(Level.SEVERE, "Error desconocido en findAll", e);
            return new ArrayList<>();
        }
    }

    @Override
    public Model find(Long id, String username, String password) {
        if (webTarget == null) return null;
        
        try {
            jakarta.ws.rs.client.Invocation.Builder request = webTarget.path(id.toString())
                    .request(MediaType.APPLICATION_JSON);
            
            if (username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
                String authString = username + ":" + password;
                String authHeader = "Basic " + Base64.getEncoder().encodeToString(authString.getBytes("UTF-8"));
                request.header("Authorization", authHeader);
            }

            Response response = request.get();

            if (response.getStatus() == 200) {
                return response.readEntity(Model.class);
            } else {
                 Logger.getLogger(ModelServiceImpl.class.getName()).log(Level.WARNING, "Error al buscar modelo {0}. Status: {1}", new Object[]{id, response.getStatus()});
            }

        } catch (Exception e) {
            Logger.getLogger(ModelServiceImpl.class.getName()).log(Level.SEVERE, "Excepción en find", e);
        }
        return null;
    }
}