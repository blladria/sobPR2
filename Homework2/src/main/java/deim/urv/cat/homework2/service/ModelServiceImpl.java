package deim.urv.cat.homework2.service;

import deim.urv.cat.homework2.model.Model;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
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
import java.util.stream.Collectors;

@ApplicationScoped
public class ModelServiceImpl implements ModelService {

    // CORRECCIÓN: Se añade "/rest" a la URL porque RESTapp.java tiene @ApplicationPath("rest")
    private static final String BASE_URI = "http://localhost:8080/PR1_sob_grup_54/rest/api/v1/models";
    
    private Client client;
    private WebTarget target;

    @PostConstruct
    public void init() {
        try {
            client = ClientBuilder.newClient();
            target = client.target(BASE_URI);
        } catch (Exception e) {
            Logger.getLogger(ModelServiceImpl.class.getName()).log(Level.SEVERE, "Error al iniciar el cliente REST", e);
        }
    }

    @Override
    public List<Model> findAll(List<String> capabilities, String provider) {
        try {
            WebTarget reqTarget = target;

            // 1. Añadir parámetro provider si existe
            if (provider != null && !provider.isEmpty()) {
                reqTarget = reqTarget.queryParam("provider", provider);
            }

            // 2. Añadir parámetros capability si existen
            if (capabilities != null && !capabilities.isEmpty()) {
                for (String cap : capabilities) {
                    reqTarget = reqTarget.queryParam("capability", cap);
                }
            }

            // 3. Hacer la petición GET a la API
            Response response = reqTarget.request(MediaType.APPLICATION_JSON).get();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                // Mapear la respuesta JSON a una lista de objetos Model
                return response.readEntity(new GenericType<List<Model>>() {});
            } else {
                Logger.getLogger(ModelServiceImpl.class.getName()).log(Level.WARNING, "La API devolvió código: {0} en URL: {1}", new Object[]{response.getStatus(), reqTarget.getUri()});
                return new ArrayList<>();
            }
        } catch (Exception e) {
            Logger.getLogger(ModelServiceImpl.class.getName()).log(Level.SEVERE, "Error conectando con la API PR1. ¿Está desplegada?", e);
            return new ArrayList<>();
        }
    }

    @Override
    public Model find(Long id, String username, String password) {
        try {
            // Construir la URL: .../models/{id}
            WebTarget pathTarget = target.path(String.valueOf(id));
            
            var requestBuilder = pathTarget.request(MediaType.APPLICATION_JSON);

            // Si hay credenciales, añadimos cabecera Authorization Basic
            if (username != null && password != null) {
                String authString = username + ":" + password;
                String authHeader = "Basic " + Base64.getEncoder().encodeToString(authString.getBytes());
                requestBuilder.header("Authorization", authHeader);
            }

            Response response = requestBuilder.get();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                return response.readEntity(Model.class);
            } else {
                // Si es 401 (No autorizado) o 404 (No encontrado), devolvemos null
                return null;
            }
        } catch (Exception e) {
            Logger.getLogger(ModelServiceImpl.class.getName()).log(Level.SEVERE, "Error buscando modelo por ID", e);
            return null;
        }
    }

    @Override
    public List<String> getUniqueCapabilities() {
        // Como la API no tiene un endpoint específico para capabilities, 
        // traemos todos los modelos y filtramos en el cliente
        List<Model> allModels = findAll(null, null);
        if (allModels == null) return new ArrayList<>();

        return allModels.stream()
                .map(Model::getMainCapability) 
                .filter(c -> c != null && !c.isEmpty())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
    
    // Método añadido para soportar el filtro de proveedores del controlador
    public List<String> getUniqueProviders() {
        List<Model> allModels = findAll(null, null);
        if (allModels == null) return new ArrayList<>();

        return allModels.stream()
                .map(Model::getProvider)
                .filter(p -> p != null && !p.isEmpty())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
}