/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.entities;


import java.io.Serializable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
/**
 *
 * @author bllad
 */
@Entity
@XmlRootElement
// NamedQuery para la autenticación: debe coincidir con la usada en RESTRequestFilter
@NamedQuery(name="Customer.findUser", 
            // APLICAR ESTE FIX AHORA MISMO
            query="SELECT c FROM Customer c WHERE UPPER(c.username) = UPPER(:username)")
public class Customer implements Serializable {
 
   private static final long serialVersionUID = 1L; 
    
    // Configuración del ID por Secuencia
    @Id
    @SequenceGenerator(name="Customer_Gen", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Customer_Gen")
    private Long id;
    
    // Credenciales (Integradas de Credentials.java)
    @Column(unique=true)
    @NotNull(message="Username can't be null")
    private String username;
    
    @NotNull(message="Password can't be null")
    private String password;
    
    private String name; 
    private String email;
    
    
    // Soporte HATEOAS 
    // Almacena el ID del último modelo consultado para generar el enlace
    private Long lastConsultedModelId;
    
    // Constructor
    public Customer() {
    }
        
    public void setName(String newName){
        this.name = newName; 
    }
    
    public void setEmail(String newEmail){
        this.email = newEmail; 
    }
    
    public String getPassword(){
        return this.password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getUsername(){
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }   
    
    public String getName(){
        return name;
    }
    
    public String getEmail(){
        return email;
    }
    
    public Long getId(){
        return id;
    }   
    
    public void setId(Long id) {
        this.id = id;
    }
    public Long getLastConsultedModelId(){
        return lastConsultedModelId;
    }
    public void setLastConsultedModelId(Long lastConsultedModelId) {
        this.lastConsultedModelId = lastConsultedModelId;
    }
}
