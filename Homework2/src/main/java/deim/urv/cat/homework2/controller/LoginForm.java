/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deim.urv.cat.homework2.controller;

import jakarta.mvc.binding.MvcBinding;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.FormParam;
import java.io.Serializable;

// SIN @Named NI @RequestScoped
public class LoginForm implements Serializable {
    @NotBlank(message = "Username required")
    @FormParam("username")
    @MvcBinding
    private String username;

    @NotBlank(message = "Password required")
    @FormParam("password")
    @MvcBinding
    private String password;
    
    // CAMBIO: Campo para guardar la URL de retorno
    @FormParam("returnUrl")
    @MvcBinding
    private String returnUrl;

    // Getters y Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getReturnUrl() { return returnUrl; }
    public void setReturnUrl(String returnUrl) { this.returnUrl = returnUrl; }
}