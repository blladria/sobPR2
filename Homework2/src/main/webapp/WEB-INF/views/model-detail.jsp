<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <title>${model.name} - Details</title>
    <link href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
</head>
<body class="d-flex flex-column min-vh-100">
    <%@ include file="layout/navbar.jsp" %>

    <div class="container mt-5 mb-5 flex-grow-1">
        <div class="row mb-5 align-items-center">
            <div class="col-md-auto text-center">
                <c:choose>
                    <c:when test="${model.logo != null && fn:startsWith(model.logo, 'http')}">
                        <img src="${model.logo}" 
                             class="rounded shadow-sm bg-white border p-2" 
                             style="width: 150px; height: 150px; object-fit: contain;" 
                             alt="${model.name}"
                             onerror="this.src='${pageContext.request.contextPath}/resources/img/Invalid.png'">
                    </c:when>
                    <c:otherwise>
                        <img src="${pageContext.request.contextPath}/resources/img/${model.logo != null ? model.logo : 'Invalid.png'}" 
                             class="rounded shadow-sm bg-white border p-2" 
                             style="width: 150px; height: 150px; object-fit: contain;"
                             alt="${model.name}"
                             onerror="this.src='${pageContext.request.contextPath}/resources/img/Invalid.png'">
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="col-md mt-3 mt-md-0">
                <h1 class="display-4 fw-bold mb-1">
                    ${model.name}
                    <c:if test="${model.private}">
                        <c:choose>
                            <c:when test="${empty sessionScope.user}">
                                <i class="bi bi-lock-fill text-warning ms-2" style="font-size: 0.6em; vertical-align: middle;" title="Private"></i>
                            </c:when>
                            <c:otherwise>
                                <i class="bi bi-unlock-fill text-success ms-2" style="font-size: 0.6em; vertical-align: middle;" title="Unlocked"></i>
                            </c:otherwise>
                        </c:choose>
                    </c:if>
                </h1>
                <p class="text-muted fs-5 mb-3">
                    Provider: <span class="fw-bold text-dark">${model.provider}</span>
                    <span class="mx-2">|</span>
                    Last updated: ${model.lastUpdated != null ? model.lastUpdated : 'N/A'}
                </p>
                <div>
                     <c:forEach var="cap" items="${model.capabilities}">
                        <span class="badge bg-primary me-1 fs-6 fw-normal">${cap}</span>
                     </c:forEach>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-lg-8">
                <div class="mb-5">
                    <h3 class="border-bottom pb-2 mb-3">Description</h3>
                    <p class="lead text-secondary">${model.description}</p>
                </div>
                
                <div class="card shadow-sm border-0 mb-4">
                    <div class="card-header bg-light fw-bold">Technical Specifications</div>
                    <div class="card-body p-0">
                        <table class="table table-striped mb-0">
                            <tbody>
                                <tr>
                                    <th class="ps-4 w-50 py-3 text-secondary">Context Length</th>
                                    <td class="pe-4 text-end py-3 fw-bold">${model.contextLength} tokens</td>
                                </tr>
                                <tr>
                                    <th class="ps-4 py-3 text-secondary">Quality Index</th>
                                    <td class="pe-4 text-end py-3 fw-bold">${model.qualityIndex}</td>
                                </tr>
                                <tr>
                                    <th class="ps-4 py-3 text-secondary">License</th>
                                    <td class="pe-4 text-end py-3 fw-bold">
                                        <span class="badge ${model.private ? 'bg-warning text-dark' : 'bg-success'}">
                                            ${model.private ? 'Private / Commercial' : 'Open Source'}
                                        </span>
                                    </td>
                                </tr>
                                <tr>
                                    <th class="ps-4 py-3 text-secondary">Input Type</th>
                                    <td class="pe-4 text-end py-3 fw-bold">${model.inputType != null ? model.inputType : 'Text'}</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            
            <div class="col-lg-4">
                <div class="card border-0 bg-light p-4 shadow-sm">
                    <h5 class="card-title fw-bold">Actions</h5>
                    <p class="card-text text-muted small">Navigation options.</p>
                    <a href="${pageContext.request.contextPath}/Web/models" class="btn btn-outline-dark w-100">
                        &larr; Back to Model List
                    </a>
                </div>
            </div>
        </div>
    </div>
    
    <%@include file="layout/footer.jsp" %>
</body>
</html>