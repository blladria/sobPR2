package deim.urv.cat.homework2.service;

import deim.urv.cat.homework2.model.User;
import deim.urv.cat.homework2.controller.UserForm;

public interface UserService {

    // Para el Login
    public User validateUser(String username, String password);

    // Para el Perfil
    public User getUser(String username, String password);

    // Para el Registro
    public boolean addUser(UserForm userForm);

    // Para actualizar el lastConsultedModelId (Requisito de la práctica)
    public void updateUser(User user, String password);
}
