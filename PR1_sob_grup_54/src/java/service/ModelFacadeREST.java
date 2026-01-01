/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import authn.Secured;
import jakarta.ejb.Stateless;
import jakarta.persistence.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.Context;
import java.util.List;
import model.entities.Model;

/**
 *
 * @author bllad
 *
 * Métodos HTTP para los Modelos Servicio RESTful para la gestión de la entidad
 * Model. URI base: /webresources/v1/models
 */
@Stateless
@Path("api/v1/models")
public class ModelFacadeREST extends AbstractFacade<Model> {

    @PersistenceContext(unitName = "sob_grup_54_PU")
    private EntityManager em;

    // Se inyecta el contexto de seguridad para verificar la identidad del usuario
    // autenticado.
    @Context
    private SecurityContext securityContext;

    public ModelFacadeREST() {
        super(Model.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    // =========================================================================
    // 1. POST /v1/models (Obligatorio con Autenticación y Validación)
    // =========================================================================
    @POST
    @Secured // Obligatorio: Requiere autenticación
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createModel(Model model) {

        // 1. Validación de negocio
        String validationError = validateModel(model);
        if (validationError != null) {
            // Retorna 400 Bad Request si la validación falla
            return Response.status(Response.Status.BAD_REQUEST).entity(validationError).build();
        }

        try {
            // 2. Persistir el modelo
            super.create(model);

            // 3. Retornar 201 Created con la URI del nuevo recurso
            String location = "v1/models/" + model.getId();

            return Response.status(Response.Status.CREATED)
                    // Agregamos el header 'Location' para cumplir con RESTful
                    .header("Location", location)
                    .entity(model) // Devolvemos el objeto creado
                    .build();

        } catch (Exception e) {
            // Error de persistencia (ej. nombre duplicado por restricción UNIQUE)
            return Response.status(Response.Status.CONFLICT)
                    .entity("Error al crear el modelo, puede que ya exista: " + e.getMessage())
                    .build();
        }
    }

    // =========================================================================
    // 2. GET /v1/models (Obligatorio con Filtros y Ordenación)
    // =========================================================================
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response findAllWithFilters(
            @QueryParam("capability") List<String> capabilities,
            @QueryParam("provider") String provider) {

        // 1. CAMBIO IMPORTANTE: Quitamos 'DISTINCT' de aquí porque rompe con campos @Lob (CLOB)
        StringBuilder queryBuilder = new StringBuilder("SELECT m FROM Model m");

        boolean hasWhere = false;

        // ... (El bloque de construcción del WHERE se queda IGUAL) ...
        if ((capabilities != null && !capabilities.isEmpty()) || (provider != null && !provider.isEmpty())) {
            queryBuilder.append(" WHERE ");
            if (provider != null && !provider.isEmpty()) {
                queryBuilder.append("m.provider = :provider");
                hasWhere = true;
            }
            if (capabilities != null && !capabilities.isEmpty()) {
                if (hasWhere) {
                    queryBuilder.append(" AND ");
                }
                queryBuilder.append("(");
                for (int i = 0; i < capabilities.size() && i < 2; i++) {
                    if (i > 0) {
                        queryBuilder.append(" OR ");
                    }
                    queryBuilder.append(":cap").append(i).append(" MEMBER OF m.capabilities");
                }
                queryBuilder.append(")");
            }
        }

        // Ordenar por nombre
        queryBuilder.append(" ORDER BY m.name ASC");

        TypedQuery<Model> query = em.createQuery(queryBuilder.toString(), Model.class);

        // ... (La inyección de parámetros se queda IGUAL) ...
        if (provider != null && !provider.isEmpty()) {
            query.setParameter("provider", provider);
        }
        if (capabilities != null && !capabilities.isEmpty()) {
            for (int i = 0; i < capabilities.size() && i < 2; i++) {
                query.setParameter("cap" + i, capabilities.get(i));
            }
        }

        // 2. CAMBIO IMPORTANTE: Obtener la lista cruda (con posibles duplicados)
        List<Model> rawList = query.getResultList();

        // 3. CAMBIO IMPORTANTE: Eliminar duplicados usando Java
        // LinkedHashSet mantiene el orden de inserción (ordenado por nombre desde la DB) pero elimina duplicados
        List<Model> distinctList = new java.util.ArrayList<>(new java.util.LinkedHashSet<>(rawList));

        // Devolver la lista limpia
        return Response.ok(distinctList).build();
    }

    // =========================================================================
    // 3. GET /v1/models/{id} (Seguridad Condicional: Solo privado requiere
    // autenticación)
    // =========================================================================
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response find(@PathParam("id") Long id) {

        Model model = super.find(id); // Obtiene el modelo de la DB

        if (model == null) {
            // Devuelve un 200 OK con un mensaje claro si no lo encuentra (temporalmente)
            return Response.ok("Modelo con ID " + id + " NO ENCONTRADO en la DB.").build();
        }

        if (!model.isPrivate()) {
            // Devuelve un 200 OK con mensaje claro si es público
            return Response.ok(model).build();
        }

        // 2. Si el modelo SÍ es privado, comprobamos la autenticación.
        if (securityContext != null && securityContext.getUserPrincipal() != null) {
            // Devuelve el objeto real si es privado y autenticado
            return Response.ok(model).build();
        } else {
            // Devuelve 401 Unauthorized (CORRECCIÓN: Debes devolver 401, no 404)
            return Response.status(Response.Status.UNAUTHORIZED)
                    .header("WWW-Authenticate", "Basic realm=\"Models Catalog\"")
                    .entity("DEBUG: Acceso denegado. Modelo PRIVADO y usuario NO AUTENTICADO.") // <-- Mensaje útil
                    .build();
        }
    }

    // =========================================================================
    // 4. PUT /v1/models/{id} (Modificar)
    // =========================================================================
    @PUT
    @Path("{id}")
    @Secured
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response edit(@PathParam("id") Long id, Model entity) {
        if (!id.equals(entity.getId())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error: El ID de la URI (" + id
                            + ") no coincide con el ID del cuerpo del modelo (" + entity.getId() + ").")
                    .build();
        }

        // Opcional: Re-validar el modelo antes de editar
        String validationError = validateModel(entity);
        if (validationError != null) {
            return Response.status(Response.Status.BAD_REQUEST).entity(validationError).build();
        }

        super.edit(entity);
        // Retorna 204 No Content para una modificación exitosa sin cuerpo de respuesta
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    // =========================================================================
    // 5. DELETE /v1/models/{id} (Eliminar)
    // =========================================================================
    @DELETE
    @Path("{id}")
    @Secured
    public Response remove(@PathParam("id") Long id) {
        Model model = super.find(id);
        if (model == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // CORRECCIÓN: Actualizar Customers que tengan este modelo como "último visto"
        // Esto evita errores de integridad referencial o datos huérfanos en Customer
        em.createQuery("UPDATE Customer c SET c.lastConsultedModelId = NULL WHERE c.lastConsultedModelId = :modelId")
                .setParameter("modelId", id)
                .executeUpdate();

        super.remove(model);

        return Response.status(Response.Status.NO_CONTENT).build();
    }

    // =========================================================================
    // Validación de Negocio
    // =========================================================================
    /**
     * Implementa las validaciones de negocio requeridas.
     *
     * @param model Modelo a validar
     * @return Mensaje de error o null si es válido.
     */
    private String validateModel(Model model) {
        // 1. Nombre obligatorio
        if (model.getName() == null || model.getName().trim().isEmpty()) {
            return "El campo 'name' es obligatorio.";
        }

        // 2. Provider obligatorio
        if (model.getProvider() == null || model.getProvider().trim().isEmpty()) {
            return "El campo 'provider' es obligatorio.";
        }

        // 3. Capabilities obligatorias y no nulas
        if (model.getCapabilities() == null || model.getCapabilities().isEmpty()) {
            return "La lista de 'capabilities' es obligatoria y no puede estar vacía.";
        }
        // Verificar que no haya capacidades vacías dentro de la lista
        for (String cap : model.getCapabilities()) {
            if (cap == null || cap.trim().isEmpty()) {
                return "La lista contiene capacidades no válidas (vacías).";
            }
        }

        // 4. Validaciones numéricas
        if (model.getContextLength() != null && model.getContextLength() < 1024) {
            return "El campo 'contextLength' debe ser >= 1024.";
        }
        if (model.getQualityIndex() != null && (model.getQualityIndex() < 0.0 || model.getQualityIndex() > 1.0)) {
            return "El 'qualityIndex' debe estar entre 0.0 y 1.0.";
        }

        return null; // Válido
    }
}
