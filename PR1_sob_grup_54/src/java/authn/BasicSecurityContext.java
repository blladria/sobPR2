/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package authn;

import java.security.Principal;
import jakarta.ws.rs.core.SecurityContext;

public class BasicSecurityContext implements SecurityContext {

    // Almacena la identidad del usuario (Principal solo requiere el nombre).
    private final Principal principal;

    // Constructor que recibe el username.
    public BasicSecurityContext(final String username) {
        this.principal = new Principal() {
            @Override
            public String getName() {
                return username;
            }
        };
    }

    @Override
    public Principal getUserPrincipal() {
        return principal;
    }

    @Override
    public boolean isUserInRole(String role) {
        // Lógica simple: No hay roles definidos, por ahora siempre false.
        return false;
    }

    @Override
    public boolean isSecure() {
        // Si usas HTTPS, esto sería true. Para desarrollo en localhost (HTTP), es false.
        return false;
    }

    @Override
    public String getAuthenticationScheme() {
        return SecurityContext.BASIC_AUTH;
    }
}
