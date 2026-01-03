package deim.urv.cat.homework2.service;

import deim.urv.cat.homework2.model.Model;
import java.util.List;

public interface ModelService {

    public List<Model> findAll(List<String> capabilities, String provider);

    public Model find(Long id, String username, String password);

    public List<String> getUniqueCapabilities();

    // Nuevo método añadido
    public List<String> getUniqueProviders();
}
