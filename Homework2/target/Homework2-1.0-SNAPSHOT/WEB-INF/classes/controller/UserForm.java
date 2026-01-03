package deim.urv.cat.homework2.controller;

import jakarta.mvc.binding.MvcBinding;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.ws.rs.FormParam;

public class UserForm {

    @MvcBinding
    @FormParam("name")
    @NotNull
    @Size(min = 2, max = 50)
    private String name;

    @MvcBinding
    @FormParam("username")
    @NotNull
    @Size(min = 3, max = 20)
    private String username;

    @MvcBinding
    @FormParam("email")
    @NotNull
    @Email
    private String email;

    @MvcBinding
    @FormParam("password")
    @NotNull
    @Size(min = 4)
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
