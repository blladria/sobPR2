package deim.urv.cat.homework2.service;

import deim.urv.cat.homework2.model.User;
import deim.urv.cat.homework2.controller.UserForm;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.client.Entity;
import java.util.logging.Logger;

public class UserServiceImpl implements UserService {
    private final WebTarget webTarget;
    private final jakarta.ws.rs.client.Client client;
    private static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class.getName());
    
    // URL CONFIRMADA POR TU LOG
    private static final String BASE_URI = "http://localhost:8080/PR1_sob_grup_54/rest/api/v1"; 
    
    public UserServiceImpl() {
        client = jakarta.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("customer");
        System.out.println("--- DEBUG: Servicio iniciado apuntando a: " + webTarget.getUri());
    }
    
    @Override
    public User findUserByEmail(String email){
        try {
            Response response = webTarget.path(email) // Ojo: Asegúrate de que tu backend soporta buscar por email así
                    .request(MediaType.APPLICATION_JSON)
                    .get();
            if (response.getStatus() == 200) {
                return response.readEntity(User.class);
            }
        } catch (Exception e) {
            // Ignoramos errores de búsqueda por ahora
        }
        return null;
    }

    @Override
    public boolean addUser(UserForm user) {
       try {
           System.out.println("--- DEBUG: Enviando usuario: " + user.getUsername());
           
           // 1. Enviar petición
           Response response = webTarget.request(MediaType.APPLICATION_JSON)
                   .post(Entity.entity(user, MediaType.APPLICATION_JSON));
           
           int status = response.getStatus();
           System.out.println("--- DEBUG: Status recibido: " + status);

           // 2. Analizar respuesta
           if (status == 201 || status == 200) {
               System.out.println("--- DEBUG: ¡ÉXITO! Usuario creado.");
               return true;
           } else {
               // 3. LEER EL ERROR REAL DEL SERVIDOR
               String errorBody = response.readEntity(String.class);
               System.out.println("--- DEBUG ERROR DEL SERVIDOR ---");
               System.out.println(errorBody);
               System.out.println("--------------------------------");
               return false;
           }
       } catch (Exception e) {
           System.out.println("--- DEBUG CRÍTICO (Excepción Cliente): " + e.getMessage());
           e.printStackTrace();
           return false;
       }
    }
}