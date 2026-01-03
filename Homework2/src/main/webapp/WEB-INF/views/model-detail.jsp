<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ca">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>${model.name} - Detall</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            body {
                background-color: #f8f9fa;
            }
            .detail-card {
                background: white;
                border-radius: 15px;
                box-shadow: 0 10px 30px rgba(0,0,0,0.05);
                border: none;
            }
            .spec-label {
                font-weight: 600;
                color: #6c757d;
                font-size: 0.85rem;
                text-transform: uppercase;
            }
            .spec-value {
                color: #212529;
                font-weight: 500;
            }
            .provider-logo-lg {
                width: 100px;
                height: 100px;
                object-fit: contain;
                border-radius: 12px;
            }
            .capability-pill {
                background: #e7f1ff;
                color: #0d6efd;
                border: 1px solid #cfe2ff;
            }
        </style>
    </head>
    <body>

        <nav class="navbar navbar-expand-lg navbar-dark bg-dark mb-4">
            <div class="container">
                <a class="navbar-brand" href="${mvc.basePath}/models">SOB AI Market</a>
                <div class="ms-auto">
                    <c:choose>
                        <c:when test="${not empty sessionScope.user}">
                            <span class="navbar-text text-white me-3">Usuari: <strong>${sessionScope.user.username}</strong></span>
                            <a href="${mvc.basePath}/logout" class="btn btn-outline-light btn-sm">Sortir</a>
                        </c:when>
                        <c:otherwise>
                            <a href="${mvc.basePath}/login" class="btn btn-outline-light btn-sm">Entrar</a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </nav>

        <div class="container mb-5">
            <nav aria-label="breadcrumb" class="mb-4">
                <ol class="breadcrumb">
                    <li class="breadcrumb-item"><a href="${mvc.basePath}/models">Models</a></li>
                    <li class="breadcrumb-item active">${model.name}</li>
                </ol>
            </nav>

            <div class="card detail-card p-4 p-md-5">
                <div class="row align-items-center mb-5">
                    <div class="col-md-2 text-center text-md-start">
                        <img src="${model.logo}" class="provider-logo-lg shadow-sm" onerror="this.src='https://via.placeholder.com/100'">
                    </div>
                    <div class="col-md-10 mt-3 mt-md-0">
                        <div class="d-flex flex-wrap align-items-center gap-3">
                            <h1 class="display-6 mb-0">${model.name}</h1>
                            <span class="badge bg-dark">v${model.version}</span>
                        </div>
                        <p class="text-muted lead mt-2">${model.provider} • <small>Actualitzat: ${model.lastUpdated}</small></p>
                    </div>
                </div>

                <div class="row g-5">
                    <div class="col-lg-8">
                        <h4 class="mb-3 fw-bold">Descripció del Model</h4>
                        <p class="text-secondary" style="line-height: 1.8; text-align: justify;">
                            ${model.description}
                        </p>

                        <h5 class="mt-5 mb-3 fw-bold">Capacitats</h5>
                        <div class="d-flex flex-wrap gap-2">
                            <c:forEach items="${model.capabilities}" var="cap">
                                <span class="badge rounded-pill capability-pill px-3 py-2">${cap}</span>
                            </c:forEach>
                        </div>
                    </div>

                    <div class="col-lg-4">
                        <div class="p-4 rounded-4 bg-light border-0">
                            <h5 class="mb-4 fw-bold">Especificacions</h5>
                            <div class="d-flex flex-column gap-3">
                                <div>
                                    <div class="spec-label">Llicència</div>
                                    <div class="spec-value">${model.license}</div>
                                </div>
                                <div>
                                    <div class="spec-label">Context Length</div>
                                    <div class="spec-value">${model.contextLength} tokens</div>
                                </div>
                                <div>
                                    <div class="spec-label">Quality Index</div>
                                    <div class="spec-value">${model.qualityIndex}/100</div>
                                </div>
                                <div>
                                    <div class="spec-label">Entrenat fins a</div>
                                    <div class="spec-value">${model.trainingDataDate}</div>
                                </div>
                                <div>
                                    <div class="spec-label">Input / Output</div>
                                    <div class="spec-value">${model.inputType} ➔ ${model.outputType}</div>
                                </div>
                            </div>
                            <hr>
                            <a href="${mvc.basePath}/models" class="btn btn-outline-secondary w-100">Tornar al llistat</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>