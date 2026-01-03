package deim.urv.cat.homework2.service;

import deim.urv.cat.homework2.model.Model;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.nio.charset.StandardCharsets; // IMPORTANTE: Para arreglar el error de compilación
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ModelServiceImpl implements ModelService {

    private final WebTarget webTarget;
    private final Client client;
    private static final Logger log = Logger.getLogger(ModelServiceImpl.class.getName());

    // Apunta a: .../api/v1
    private static final String BASE_URI = "http://localhost:8080/PR1_sob_grup_54/rest/api/v1";

    public ModelServiceImpl() {
        client = ClientBuilder.newClient();
        // Resultado final: http://localhost:8080/PR1_sob_grup_54/rest/api/v1/models
        webTarget = client.target(BASE_URI).path("models");
    }

    @Override
    public List<Model> findAll(List<String> capabilities, String provider) {
        // En este método NO filtramos por privado. Mostramos todo lo que devuelva la API.
        try {
            Response response = webTarget.request(MediaType.APPLICATION_JSON).get();

            if (response.getStatus() != 200) {
                log.log(Level.SEVERE, "Error conectando a {0}. Status: {1}",
                        new Object[]{webTarget.getUri(), response.getStatus()});
                return new ArrayList<>();
            }

            List<Model> allModels = response.readEntity(new GenericType<List<Model>>() {
            });

            return allModels.stream()
                    .filter(model -> {
                        // Filtro Provider
                        boolean matchProvider = (provider == null || provider.trim().isEmpty())
                                || (model.getProvider() != null && model.getProvider().trim().equalsIgnoreCase(provider.trim()));

                        // Filtro Capabilities
                        boolean matchCapability = true;
                        if (capabilities != null && !capabilities.isEmpty()) {
                            if (model.getCapabilities() == null || model.getCapabilities().isEmpty()) {
                                matchCapability = false;
                            } else {
                                matchCapability = capabilities.stream().anyMatch(filterCap
                                        -> model.getCapabilities().stream()
                                                .anyMatch(modelCap -> modelCap.trim().equalsIgnoreCase(filterCap.trim()))
                                );
                            }
                        }
                        return matchProvider && matchCapability;
                    })
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.log(Level.SEVERE, "Error crítico en ModelService:", e);
            return new ArrayList<>();
        }
    }

    @Override
    public Model find(Long id, String username, String password) {
        try {
            // CAMBIO IMPORTANTE: Si tenemos credenciales, las usamos.
            jakarta.ws.rs.client.Invocation.Builder request = webTarget.path(String.valueOf(id))
                    .request(MediaType.APPLICATION_JSON);

            if (username != null && password != null && !username.isEmpty()) {
                String authString = username + ":" + password;
                // CORRECCIÓN AQUÍ: Usamos StandardCharsets.UTF_8 para evitar el error de compilación
                String authHeader = "Basic " + Base64.getEncoder().encodeToString(authString.getBytes(StandardCharsets.UTF_8));
                request.header("Authorization", authHeader);
            }

            return request.get(Model.class);

        } catch (Exception e) {
            log.log(Level.SEVERE, "Error buscando modelo por ID: " + id + " - " + e.getMessage());
            throw e;
        }
    }

    @Override
    public List<String> getUniqueCapabilities() {
        List<Model> models = findAll(null, null);
        if (models == null) {
            return new ArrayList<>();
        }

        return models.stream()
                .filter(m -> m.getCapabilities() != null)
                .flatMap(m -> m.getCapabilities().stream())
                .map(String::trim)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getUniqueProviders() {
        List<Model> models = findAll(null, null);
        if (models == null) {
            return new ArrayList<>();
        }

        return models.stream()
                .map(Model::getProvider)
                .filter(p -> p != null && !p.isEmpty())
                .map(String::trim)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
}
