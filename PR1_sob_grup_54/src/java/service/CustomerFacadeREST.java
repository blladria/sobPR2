/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import authn.Secured;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import model.entities.Customer;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.core.Context;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author bllad Métodos HTTP para los Usuarios Servicio RESTful para la gestión
 *         de la entidad Customer. URI base: /webresources/v1/customer
 */
@Stateless
@Path("api/v1/customer")
public class CustomerFacadeREST extends AbstractFacade<Customer> {

    @PersistenceContext(unitName = "sob_grup_54_PU")
    private EntityManager em;

    // Inyectamos UriInfo para construir el enlace HATEOAS
    @Context
    private UriInfo uriInfo;

    public CustomerFacadeREST() {
        super(Customer.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    // =========================================================================
    // 1. GET /v1/customer (Listar todos)
    // =========================================================================
    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Response findAllCustomers() {

        // 1. Obtener todos los clientes
        List<Customer> customers = super.findAll();

        // 2. Mapear a objetos seguros
        List<Map<String, Object>> safeCustomerList = new ArrayList<>();

        for (Customer customer : customers) {
            Map<String, Object> safeData = new HashMap<>();

            safeData.put("id", customer.getId());
            safeData.put("username", customer.getUsername());
            safeData.put("name", customer.getName());
            safeData.put("email", customer.getEmail());
            // NO password
            safeData.put("lastConsultedModelId", customer.getLastConsultedModelId());

            safeCustomerList.add(safeData);
        }

        // Retornar Response
        return Response.ok(safeCustomerList).build();
    }

    // =========================================================================
    // 2. GET /v1/customer/{id} (Detalles con HATEOAS)
    // =========================================================================
    @GET
    @Path("{id}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Response find(@PathParam("id") Long id) {
        Customer customer = super.find(id);

        if (customer == null) {
            return Response.status(Response.Status.NOT_FOUND).build(); // 404
        }

        // 1. CREAR MAPA PARA DATOS SEGUROS
        Map<String, Object> customerData = new HashMap<>();

        // Añadir campos seguros directamente de la entidad Customer
        customerData.put("id", customer.getId());
        customerData.put("username", customer.getUsername());
        customerData.put("name", customer.getName());
        customerData.put("email", customer.getEmail());

        // 2. GENERAR Y ADJUNTAR EL ENLACE HATEOAS
        Long lastModelId = customer.getLastConsultedModelId();

        if (lastModelId != null) {
            String baseUri = uriInfo.getBaseUri().toString();
            String modelLink = baseUri + "v1/models/" + lastModelId.toString(); // Generación simple

            Map<String, String> links = new HashMap<>();
            links.put("model", modelLink);
            customerData.put("links", links);
        }

        // Retornar el objeto Map (serializado como JSON)
        return Response.ok(customerData).build();
    }

    // =========================================================================
    // 3. PUT /v1/customer/{id} (Opcional - Modificar)
    // =========================================================================
    @PUT
    @Path("{id}")
    @Secured
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Response edit(@PathParam("id") Long id, Customer entity) {
        // 1. Verificar coincidencia de IDs
        if (!id.equals(entity.getId())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("El ID de la URI no coincide con el ID del cuerpo del cliente.")
                    .build();
        }

        // 2. Buscar cliente existente
        Customer existingCustomer = super.find(id);
        if (existingCustomer == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // 3. Actualizar campos (Solo si vienen en el JSON y no están vacíos)

        // Actualizar Nombre
        if (entity.getName() != null) {
            existingCustomer.setName(entity.getName());
        }

        // Actualizar Email
        if (entity.getEmail() != null) {
            existingCustomer.setEmail(entity.getEmail());
        }

        // Permitir actualizar Username y Password 

        if (entity.getUsername() != null && !entity.getUsername().trim().isEmpty()) {
            existingCustomer.setUsername(entity.getUsername());
        }

        if (entity.getPassword() != null && !entity.getPassword().trim().isEmpty()) {
            existingCustomer.setPassword(entity.getPassword());
        }

        try {
            super.edit(existingCustomer);
            return Response.status(Response.Status.NO_CONTENT).build();

        } catch (Exception e) {
            // Capturar error si intenta poner un username que ya existe (Violación Unique)
            return Response.status(Response.Status.CONFLICT)
                    .entity("No se pudo actualizar. Es posible que el 'username' ya esté en uso.")
                    .build();
        }
    }

}
