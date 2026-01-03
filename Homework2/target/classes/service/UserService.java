package deim.urv.cat.homework2.service;

import deim.urv.cat.homework2.model.User;
import deim.urv.cat.homework2.controller.UserForm;

public interface UserService {

    // Método principal para Login (usado por LoginController)
    public User validateUser(String username, String password);

    // Alias o método para obtener datos frescos (usado por ProfileController)
    public User getUser(String username, String password);

    // Método para registro (usado por SignUpController)
    public boolean addUser(UserForm userForm);
}
