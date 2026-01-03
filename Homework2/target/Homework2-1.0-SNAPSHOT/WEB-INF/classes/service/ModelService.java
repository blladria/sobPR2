package deim.urv.cat.homework2.service;

import deim.urv.cat.homework2.model.Model;
import java.util.List;

public interface ModelService {
    // Buscar con filtros
    public List<Model> findAll(List<String> capabilities, String provider);
    
    // Buscar un modelo concreto
    public Model find(Long id, String username, String password);
    
    // Obtener lista de capabilities únicas (necesario para el filtro)
    public List<String> getUniqueCapabilities();
}