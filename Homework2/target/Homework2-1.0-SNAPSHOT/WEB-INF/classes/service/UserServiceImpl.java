package deim.urv.cat.homework2.service;

import deim.urv.cat.homework2.model.User;
import deim.urv.cat.homework2.controller.UserForm;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.client.Entity;
import java.util.logging.Logger;
import java.util.logging.Level;

public class UserServiceImpl implements UserService {
    private final WebTarget webTarget;
    private final jakarta.ws.rs.client.Client client;
    private static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class.getName());
    
    // CAMBIO IMPORTANTE: Asegúrate de que esta URL NO termina en /customer
    private static final String BASE_URI = "http://localhost:8080/PR1_sob_grup_54/rest/api/v1"; 
    
    public UserServiceImpl() {
        client = jakarta.ws.rs.client.ClientBuilder.newClient();
        // Aquí se añade /customer. La URL final debe ser .../api/v1/customer
        webTarget = client.target(BASE_URI).path("customer");
        
        // IMPRIMIR URL EN CONSOLA PARA DEPURAR
        System.out.println("--- DEBUG: Conectando a URL: " + webTarget.getUri().toString() + " ---");
    }
    
    @Override
    public User findUserByEmail(String email){
        try {
            Response response = webTarget.path(email)
                    .request(MediaType.APPLICATION_JSON)
                    .get();
            if (response.getStatus() == 200) {
                return response.readEntity(User.class);
            }
        } catch (Exception e) {
            // Imprimir error si falla la conexión
            System.out.println("--- DEBUG ERROR en findUserByEmail: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean addUser(UserForm user) {
       try {
           System.out.println("--- DEBUG: Intentando registrar usuario: " + user.getUsername());
           
           Response response = webTarget.request(MediaType.APPLICATION_JSON)
                   .post(Entity.entity(user, MediaType.APPLICATION_JSON));
           
           System.out.println("--- DEBUG: Respuesta del servidor: " + response.getStatus());
           
           if (response.getStatus() == 201) {
               return true;
           } else {
               // Imprimir por qué ha fallado
               System.out.println("--- DEBUG: Fallo al crear. Código: " + response.getStatus());
               return false;
           }
       } catch (Exception e) {
           System.out.println("--- DEBUG ERROR en addUser: " + e.getMessage());
           e.printStackTrace();
           return false;
       }
    }
}