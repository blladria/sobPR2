/*
 * Archivo: Homework2/src/main/java/deim/urv/cat/homework2/service/ModelServiceImpl.java
 * Ubicación: Frontend (Cliente MVC)
 */
package deim.urv.cat.homework2.service;

import deim.urv.cat.homework2.model.Model;
import jakarta.ws.rs.NotAuthorizedException;
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

    // Asegúrate de que este puerto (8080) y nombre de proyecto (PR1_sob_grup_54) son correctos según tu backend
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
        if (webTarget == null) {
            return new ArrayList<>();
        }

        try {
            WebTarget target = webTarget;

            // 1. Aplicar Filtro Provider (Solo si no es null ni vacío)
            if (provider != null && !provider.trim().isEmpty()) {
                target = target.queryParam("provider", provider);
            }

            // 2. Aplicar Filtro Capabilities (CORREGIDO)
            if (capabilities != null && !capabilities.isEmpty()) {
                for (String cap : capabilities) {
                    // --- CORRECCIÓN AQUÍ ---
                    // El formulario envía cadenas vacías ("") si se selecciona "All Capabilities".
                    // Debemos ignorarlas para no romper la query en el backend.
                    if (cap != null && !cap.trim().isEmpty()) {
                        target = target.queryParam("capability", cap);
                    }
                }
            }

            // LOG PARA DIAGNÓSTICO: Ver qué URL estamos llamando realmente
            Logger.getLogger(ModelServiceImpl.class.getName()).log(Level.INFO, "Llamando al Backend: {0}", target.getUri());

            // 3. Hacer la petición GET
            Response response = target.request(MediaType.APPLICATION_JSON).get();

            if (response.getStatus() == 200) {
                return response.readEntity(new GenericType<List<Model>>() {
                });
            } else {
                Logger.getLogger(ModelServiceImpl.class.getName()).log(Level.WARNING, "Backend respondió con error en findAll: {0}", response.getStatus());
                return new ArrayList<>();
            }

        } catch (ProcessingException e) {
            Logger.getLogger(ModelServiceImpl.class.getName()).log(Level.SEVERE, "Error procesando JSON o conectando al backend.", e);
            return new ArrayList<>();
        } catch (Exception e) {
            Logger.getLogger(ModelServiceImpl.class.getName()).log(Level.SEVERE, "Error desconocido en findAll", e);
            return new ArrayList<>();
        }
    }

    @Override
    public Model find(Long id, String username, String password) {
        if (webTarget == null) {
            return null;
        }

        try {
            jakarta.ws.rs.client.Invocation.Builder request = webTarget.path(id.toString())
                    .request(MediaType.APPLICATION_JSON);

            // Añadir cabecera de autenticación si existen credenciales
            if (username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
                try {
                    String authString = username + ":" + password;
                    String authHeader = "Basic " + Base64.getEncoder().encodeToString(authString.getBytes("UTF-8"));
                    request.header("Authorization", authHeader);
                } catch (Exception ex) {
                    Logger.getLogger(ModelServiceImpl.class.getName()).log(Level.SEVERE, "Error codificando autenticación", ex);
                }
            }

            Response response = request.get();

            // CASO ÉXITO
            if (response.getStatus() == 200) {
                return response.readEntity(Model.class);
            } // CASO NO AUTORIZADO (Backend devuelve 401)
            // Lanzamos excepción para que el controlador pueda redirigir al login
            else if (response.getStatus() == 401) {
                throw new NotAuthorizedException("El usuario no tiene permiso para ver este modelo privado.");
            } // OTROS ERRORES (404 Not Found, 500 Server Error)
            else {
                Logger.getLogger(ModelServiceImpl.class.getName()).log(Level.WARNING, "Error al buscar modelo {0}. Status: {1}", new Object[]{id, response.getStatus()});
            }

        } catch (NotAuthorizedException e) {
            throw e; // Re-lanzamos para que el Controller la capture
        } catch (Exception e) {
            Logger.getLogger(ModelServiceImpl.class.getName()).log(Level.SEVERE, "Excepción en find", e);
        }
        return null;
    }
}
