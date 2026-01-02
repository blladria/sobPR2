/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deim.urv.cat.homework2.service;

import deim.urv.cat.homework2.model.Model;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Base64; // Para Basic Auth manual si fuera necesario

public class ModelServiceImpl implements ModelService {

    private final WebTarget webTarget;
    private final Client client;
    
    // IMPORTANTE: Verifica que este nombre (PR1_sob_grup_54) es el de tu deploy del Backend
    private static final String BASE_URI = "http://localhost:8080/PR1_sob_grup_54/rest/api/v1"; 

    public ModelServiceImpl() {
        client = ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("models");
    }

    @Override
    public List<Model> findAll(List<String> capabilities, String provider) {
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
                System.out.println("Error al obtener modelos. Status: " + response.getStatus());
                return new ArrayList<>();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public Model find(Long id, String username, String password) {
        try {
            // Construir petición
            jakarta.ws.rs.client.Invocation.Builder request = webTarget.path(id.toString())
                    .request(MediaType.APPLICATION_JSON);
            
            // Si tenemos usuario y contraseña, añadimos cabecera Basic Auth
            if (username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
                String authString = username + ":" + password;
                String authHeader = "Basic " + Base64.getEncoder().encodeToString(authString.getBytes("UTF-8"));
                request.header("Authorization", authHeader);
            }

            // Hacer petición GET
            Response response = request.get();

            if (response.getStatus() == 200) {
                return response.readEntity(Model.class);
            } else if (response.getStatus() == 401) {
                // No autorizado (Modelo Privado sin login)
                System.out.println("Acceso denegado al modelo " + id);
                return null; 
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
