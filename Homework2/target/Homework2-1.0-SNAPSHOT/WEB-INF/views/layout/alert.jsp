<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%-- CAMBIO: El div solo se crea si flashMessage.text existe y NO está vacío --%>
<c:if test="${not empty flashMessage and not empty flashMessage.text}">
    <div class="alert alert-${flashMessage.type} alert-dismissible fade show" role="alert">
        ${flashMessage.text}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
</c:if>

<%-- CAMBIO: El bloque de errores solo aparece si la lista de errores NO está vacía --%>
<c:if test="${not empty errors}">
    <c:forEach var="error" items="${errors}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <strong>${error.paramName}:</strong> ${error.message}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:forEach>
</c:if>