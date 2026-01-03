package deim.urv.cat.homework2.model;

public class User {

    private String username;
    private String password;
    private String name;
    private String email;

    private Long id;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String fixNull(String in) {
        return (in == null) ? "" : in;
    }
}
