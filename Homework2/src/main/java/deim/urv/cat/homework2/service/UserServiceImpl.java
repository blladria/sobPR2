package deim.urv.cat.homework2.service;

import deim.urv.cat.homework2.model.User;
import deim.urv.cat.homework2.controller.UserForm;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.client.Entity;

public class UserServiceImpl implements UserService {
    private final WebTarget webTarget;
    private final jakarta.ws.rs.client.Client client;
    
    // CORRECCIÓN: Apuntamos a la base de la API, sin repetir 'customer'
    private static final String BASE_URI = "http://localhost:8080/PR1_sob_grup_54/rest/api/v1";
    
    public UserServiceImpl() {
        client = jakarta.ws.rs.client.ClientBuilder.newClient();
        // CORRECCIÓN: Aquí añadimos 'customer', resultando en .../api/v1/customer
        webTarget = client.target(BASE_URI).path("customer");
    }
    
    @Override
    public User findUserByEmail(String email){
        // Busca por email (usado para validación previa en el controller)
        // Nota: Tu back-end debe soportar búsqueda por ID o tener un endpoint de búsqueda
        // Si falla, retornará null y el controller seguirá.
        try {
            Response response = webTarget.path(email)
                    .request(MediaType.APPLICATION_JSON)
                    .get();
            if (response.getStatus() == 200) {
                return response.readEntity(User.class);
            }
        } catch (Exception e) {
            // Ignoramos errores de conexión en esta comprobación opcional
        }
        return null;
    }

    @Override
    public boolean addUser(UserForm user) {
       // Envía el POST para crear el usuario
       Response response = webTarget.request(MediaType.APPLICATION_JSON)
               .post(Entity.entity(user, MediaType.APPLICATION_JSON));
               
       // Devuelve TRUE solo si se creó (201 Created)
       return response.getStatus() == 201;
    }
}