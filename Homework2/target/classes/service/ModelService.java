/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deim.urv.cat.homework2.service;

import deim.urv.cat.homework2.model.Model;
import java.util.List;

public interface ModelService {
    // Obtener todos los modelos, opcionalmente filtrados por capacidad y proveedor
    public List<Model> findAll(List<String> capabilities, String provider);
    
    // Obtener un modelo por su ID (usando credenciales si es necesario)
    public Model find(Long id, String username, String password);
}
