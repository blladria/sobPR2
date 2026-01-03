<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <title>${model.name} - Detalle</title>
    <link href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <%@ include file="layout/navbar.jsp" %>

    <div class="container mt-4 mb-5">
        <div class="row mb-4">
            <div class="col-auto">
                <img src="${pageContext.request.contextPath}/resources/img/${model.logo}" 
                     class="img-thumbnail" style="width: 100px; height: 100px; object-fit: contain;" 
                     onerror="this.src='${pageContext.request.contextPath}/resources/img/default.png'">
            </div>
            <div class="col">
                <h1 class="display-6 fw-bold">
                    ${model.name} 
                    <span class="badge bg-secondary fs-6 align-middle">Version: 2025-06-10</span> </h1>
                <p class="text-muted fs-5">
                    ${model.provider} &bull; Last updated July 2025
                </p>
            </div>
        </div>

        <div class="row mb-4">
            <div class="col-12">
                <p class="lead">${model.description}</p>
                <p>
                    <c:forEach var="cap" items="${model.capabilities}" end="2">
                        <span class="badge rounded-pill bg-outline-primary border border-primary text-primary me-2 p-2">
                            ${cap}
                        </span>
                    </c:forEach>
                </p>
            </div>
        </div>

        <div class="card shadow-sm">
            <div class="card-header bg-white">
                <h3 class="mb-0">Model Specifications</h3>
            </div>
            <div class="card-body p-0">
                <table class="table table-hover mb-0">
                    <tbody>
                        <tr>
                            <th scope="row" class="ps-4 w-50">Context Length</th>
                            <td class="pe-4 text-end fw-bold">${model.contextLength}</td>
                        </tr>
                        <tr>
                            <th scope="row" class="ps-4">Quality Index</th>
                            <td class="pe-4 text-end fw-bold">${model.qualityIndex}</td>
                        </tr>
                        <tr>
                            <th scope="row" class="ps-4">License</th>
                            <td class="pe-4 text-end fw-bold">${model.private ? 'Private / Custom' : 'Open Source'}</td>
                        </tr>
                        <tr>
                            <th scope="row" class="ps-4">Input Type</th>
                            <td class="pe-4 text-end fw-bold">Text, Image</td> </tr>
                        <tr>
                            <th scope="row" class="ps-4">Publisher</th>
                            <td class="pe-4 text-end fw-bold">${model.provider}</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
        
        <div class="mt-4">
            <a href="${pageContext.request.contextPath}/models" class="btn btn-outline-dark">← Volver al listado</a>
        </div>
    </div>
</body>
</html>