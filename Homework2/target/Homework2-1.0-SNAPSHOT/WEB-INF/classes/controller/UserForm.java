package deim.urv.cat.homework2.controller;

import jakarta.mvc.binding.MvcBinding;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.ws.rs.FormParam;
import java.io.Serializable;

// SOLUCIÓN ERROR 500: Eliminamos @Named y @RequestScoped.
// Al ser un POJO simple, no intenta inyectar los @FormParam durante la petición GET (cargar formulario),
// evitando la excepción "IllegalStateException: The @FormParam is utilized when the request method is GET".
public class UserForm implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Username is required")
    @FormParam("username")
    @MvcBinding
    @Size(min = 2, max = 30, message = "Username must be between 2 and 30 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @FormParam("password")
    @MvcBinding
    @Size(min = 4, message = "Password must be at least 4 characters")
    private String password;

    @NotBlank(message = "Name is required")
    @FormParam("name")
    @MvcBinding
    private String name;

    @NotBlank(message = "Email is required")
    @FormParam("email")
    @Email(message = "Email format is not valid")
    @Pattern(regexp = "^[^@]+@[^@]+\\.[a-zA-Z]{2,}$", message = "Email must be valid (example@domain.com)")
    @MvcBinding
    private String email;

    public String getUsername() {
        return fixNull(username);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return fixNull(password);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return fixNull(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return fixNull(email);
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String fixNull(String in) {
        return (in == null) ? "" : in;
    }
}
