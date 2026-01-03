<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ca">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>AI Models - Part Pública</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            body {
                background-color: #f4f7f6;
            }
            .navbar {
                margin-bottom: 2rem;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            }

            /* Estilos de la Graella (Grid) */
            .model-card {
                height: 100%;
                border: none;
                background: white;
                padding: 1.5rem;
                border-radius: 12px;
                transition: all 0.3s ease;
                display: flex;
                flex-direction: column;
                box-shadow: 0 4px 6px rgba(0,0,0,0.05);
            }
            .model-card:hover {
                transform: translateY(-5px);
                box-shadow: 0 8px 15px rgba(0,0,0,0.1);
            }

            .provider-logo {
                width: 50px;
                height: 50px;
                object-fit: contain;
                border-radius: 8px;
                background: #f8f9fa;
                padding: 5px;
            }

            .private-badge {
                color: #dc3545;
                font-size: 1.1rem;
                margin-left: auto;
            }

            .model-link {
                text-decoration: none;
                color: #2c3e50;
                font-weight: 600;
            }
            .model-link:hover {
                color: #007bff;
            }

            .summary-text {
                font-size: 0.9rem;
                color: #6c757d;
                margin-top: 10px;
                display: -webkit-box;
                -webkit-line-clamp: 3; /* Máximo 3 líneas para el resumen */
                -webkit-box-orient: vertical;
                overflow: hidden;
            }

            .badge-capability {
                background-color: #e9ecef;
                color: #495057;
                font-weight: 500;
                font-size: 0.75rem;
                text-transform: uppercase;
                letter-spacing: 0.5px;
            }
        </style>
    </head>
    <body>

        <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
            <div class="container">
                <a class="navbar-brand fw-bold" href="${mvc.basePath}/models">SOB AI Market</a>
                <div class="ms-auto d-flex align-items-center">
                    <c:choose>
                        <c:when test="${not empty sessionScope.user}">
                            <span class="navbar-text text-white me-3">
                                <a href="${mvc.basePath}/profile" class="text-white text-decoration-none">
                                    Benvingut <strong>${sessionScope.user.username}</strong>!
                                </a>
                            </span>
                            <a href="${mvc.basePath}/logout" class="btn btn-outline-light btn-sm">Logout</a>
                        </c:when>
                        <c:otherwise>
                            <a href="${mvc.basePath}/login" class="btn btn-outline-light btn-sm me-2">Login</a>
                            <a href="${mvc.basePath}/SignUp" class="btn btn-primary btn-sm">Sign Up</a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </nav>

        <div class="container">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h1>Explora els Models</h1>
            </div>

            <div class="card card-body mb-5 border-0 shadow-sm">
                <form method="get" action="${mvc.basePath}/models" class="row g-3">
                    <div class="col-md-5">
                        <label class="form-label small fw-bold text-muted">PROVEÏDOR</label>
                        <select name="provider" class="form-select">
                            <option value="">Tots els proveïdors</option>
                            <c:forEach items="${allProviders}" var="p">
                                <option value="${p}" ${param.provider == p ? 'selected' : ''}>${p}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-5">
                        <label class="form-label small fw-bold text-muted">CAPACITAT</label>
                        <select name="capability" class="form-select">
                            <option value="">Totes les capacitats</option>
                            <c:forEach items="${allCapabilities}" var="c">
                                <option value="${c}" ${param.capability == c ? 'selected' : ''}>${c}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-2 d-flex align-items-end">
                        <button type="submit" class="btn btn-primary w-100">Filtrar</button>
                    </div>
                </form>
            </div>

            <div class="row row-cols-1 row-cols-md-3 g-4 mb-5">
                <c:forEach items="${modelList}" var="m">
                    <div class="col">
                        <div class="model-card">
                            <div class="d-flex align-items-center mb-2">
                                <img src="${m.logo}" class="provider-logo me-3" onerror="this.src='https://via.placeholder.com/50?text=AI'">
                                <h5 class="mb-0 flex-grow-1">
                                    <a href="${mvc.basePath}/models/${m.id}" class="model-link">${m.name}</a>
                                </h5>
                                <c:if test="${m.private}">
                                    <span class="private-badge" title="Model Privat">🔒</span>
                                </c:if>
                            </div>

                            <p class="summary-text">
                                ${m.summary}
                            </p>

                            <div class="mt-auto pt-3">
                                <span class="badge badge-capability">${m.mainCapability}</span>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>

            <c:if test="${empty modelList}">
                <div class="alert alert-warning text-center shadow-sm">
                    No s'han trobat models que coincideixin amb la cerca.
                </div>
            </c:if>
        </div>

        <footer class="py-4 bg-white border-top mt-auto">
            <div class="container text-center text-muted">
                <small>Sistemes Oberts 2026 - ETSE URV</small>
            </div>
        </footer>

    </body>
</html>