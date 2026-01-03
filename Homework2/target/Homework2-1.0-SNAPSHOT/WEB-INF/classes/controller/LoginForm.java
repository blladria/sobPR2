package deim.urv.cat.homework2.controller;

import jakarta.ws.rs.FormParam;
import jakarta.validation.constraints.NotEmpty;

public class LoginForm {
    @NotEmpty(message = "El usuario es obligatorio")
    @FormParam("username")
    private String username;

    @NotEmpty(message = "La contraseña es obligatoria")
    @FormParam("password")
    private String password;
    
    // Campo oculto para saber dónde volver
    @FormParam("returnUrl")
    private String returnUrl;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getReturnUrl() { return returnUrl; }
    public void setReturnUrl(String returnUrl) { this.returnUrl = returnUrl; }
}