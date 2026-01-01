package authn;

import com.sun.xml.messaging.saaj.util.Base64;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.StringTokenizer;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.NoResultException;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.ext.Provider;
import jakarta.annotation.Priority;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.container.ResourceInfo;
import model.entities.Customer;

/**
 * @author Marc Sanchez
 */
@Priority(Priorities.AUTHENTICATION)
@Provider
public class RESTRequestFilter implements ContainerRequestFilter {

    private static final String AUTHORIZATION_HEADER_PREFIX = "Basic ";

    // to access the resource class and resource method matched by the current request
    @Context
    private ResourceInfo resourceInfo;

    @PersistenceContext(unitName = "sob_grup_54_PU")
    private EntityManager em;

    @Override
    public void filter(ContainerRequestContext requestCtx) throws IOException {
        Method method = resourceInfo.getResourceMethod();

        // 1. Obtener el header de autorización
        List<String> headers = requestCtx.getHeaders().get(HttpHeaders.AUTHORIZATION);
        String username = null;
        String password = null;

        // Solo procedemos si existe el header Authorization
        if (headers != null && !headers.isEmpty()) {
            try {
                // Lógica de decodificación de Basic Auth para obtener username y password
                String authHeader = headers.get(0);

                if (authHeader.startsWith(AUTHORIZATION_HEADER_PREFIX)) {
                    String auth = authHeader.substring(AUTHORIZATION_HEADER_PREFIX.length()).trim();

                    // NOTA: Debes asegurarte de que el import para Base64 es correcto (jakarta o java.util.Base64)
                    String decode = new String(java.util.Base64.getDecoder().decode(auth));

                    StringTokenizer tokenizer = new StringTokenizer(decode, ":");
                    username = tokenizer.nextToken();
                    password = tokenizer.nextToken();

                    // 2. Intentar autenticar y establecer el SecurityContext
                    try {
                        // FIX CRÍTICO: Usar la Named Query correcta: "Customer.findUser"
                        TypedQuery<Customer> query = em.createNamedQuery("Customer.findUser", Customer.class);
                        Customer c = query.setParameter("username", username).getSingleResult();

                        if (c.getPassword().equals(password)) {
                            // Éxito: Establece el SecurityContext
                            // Esto hace que securityContext.getUserPrincipal() != null sea verdadero en ModelFacadeREST.find
                            requestCtx.setSecurityContext(new BasicSecurityContext(username));

                            // IMPORTANTE: NO hacemos 'return' aquí. El flujo debe continuar al punto 3.
                        }
                        // Si falla la contraseña, el SecurityContext no se establece, y el flujo continúa.

                    } catch (NoResultException e) {
                        // Usuario no encontrado. El SecurityContext no se establece, y el flujo continúa.
                    }
                }
            } catch (Exception e) {
                // Error de decodificación o formato incorrecto (400 Bad Request)
                requestCtx.abortWith(
                        Response.status(Response.Status.BAD_REQUEST).build()
                );
                return;
            }
        }

        // 3. APLICAR RESTRICCIÓN @Secured (SOLO si el método REST lo tiene)
        Secured secured = method.getAnnotation(Secured.class);

        if (secured != null) {
            // Este es un método restringido. Verificamos si el contexto fue establecido en el paso 2.
            if (requestCtx.getSecurityContext() == null
                    || requestCtx.getSecurityContext().getUserPrincipal() == null) {

                // Falla: No hay SecurityContext (no autenticado o credenciales inválidas)
                requestCtx.abortWith(
                        Response.status(Response.Status.UNAUTHORIZED)
                                .header(HttpHeaders.WWW_AUTHENTICATE, "Basic realm=\"Models Catalog\"")
                                .build()
                );
            }
            // Si el contexto existe, el método @Secured pasa.
        }

        // Si el método NO es @Secured (como find), o si @Secured pasó la verificación, el filtro termina 
        // y la solicitud llega al recurso REST con el SecurityContext correctamente establecido.
    }
}
