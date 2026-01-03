<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ca">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Perfil de l'Usuari - AI Market</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            body {
                background-color: #f0f2f5;
            }
            .profile-card {
                border-radius: 15px;
                border: none;
                box-shadow: 0 4px 12px rgba(0,0,0,0.1);
                background: white;
            }
            .user-avatar {
                width: 100px;
                height: 100px;
                background: #0d6efd;
                color: white;
                display: flex;
                align-items: center;
                justify-content: center;
                font-size: 3rem;
                border-radius: 50%;
                margin: 0 auto 1rem;
            }
            .model-preview {
                background: #ffffff;
                border-left: 5px solid #0d6efd;
                border-radius: 8px;
                transition: transform 0.2s;
            }
            .model-preview:hover {
                transform: scale(1.01);
            }
            .provider-logo-sm {
                width: 40px;
                height: 40px;
                object-fit: contain;
            }
        </style>
    </head>
    <body>

        <nav class="navbar navbar-expand-lg navbar-dark bg-dark mb-4">
            <div class="container">
                <a class="navbar-brand" href="${mvc.basePath}/models">SOB AI Market</a>
                <div class="ms-auto">
                    <a href="${mvc.basePath}/logout" class="btn btn-outline-light btn-sm">Sortir</a>
                </div>
            </div>
        </nav>

        <div class="container">
            <div class="row justify-content-center">
                <div class="col-md-8">
                    <div class="card profile-card p-4 mb-4 text-center">
                        <div class="user-avatar">
                            <c:out value="${userProfile.username.substring(0,1).toUpperCase()}"/>
                        </div>
                        <h2 class="fw-bold"><c:out value="${userProfile.username}"/></h2>
                        <p class="text-muted"><c:out value="${userProfile.email}"/></p>

                        <div class="row mt-4 text-start">
                            <div class="col-6 mb-3">
                                <small class="text-muted d-block text-uppercase fw-bold" style="font-size: 0.7rem;">Nom</small>
                                <span class="fw-bold"><c:out value="${userProfile.name}"/></span>
                            </div>
                            <div class="col-6 mb-3">
                                <small class="text-muted d-block text-uppercase fw-bold" style="font-size: 0.7rem;">Cognoms</small>
                                <span class="fw-bold"><c:out value="${userProfile.surname}"/></span>
                            </div>
                        </div>
                    </div>

                    <h4 class="mb-3 fw-bold">Últim model consultat</h4>
                    <c:choose>
                        <c:when test="${not empty lastModel}">
                            <div class="card profile-card model-preview p-3 shadow-sm mb-4">
                                <div class="d-flex align-items-center">
                                    <img src="${lastModel.logo}" class="provider-logo-sm me-3" onerror="this.src='https://via.placeholder.com/40'">
                                    <div class="flex-grow-1">
                                        <h5 class="mb-0">
                                            <a href="${mvc.basePath}/models/${lastModel.id}" class="text-decoration-none text-dark fw-bold">
                                                <c:out value="${lastModel.name}"/>
                                            </a>
                                        </h5>
                                        <small class="text-muted"><c:out value="${lastModel.provider}"/> • <c:out value="${lastModel.mainCapability}"/></small>
                                    </div>
                                    <div class="text-end">
                                        <a href="${mvc.basePath}/models/${lastModel.id}" class="btn btn-primary btn-sm">Veure detall</a>
                                    </div>
                                </div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="alert alert-info border-0 shadow-sm">
                                Encara no has consultat cap model.
                            </div>
                        </c:otherwise>
                    </c:choose>

                    <div class="text-center mt-2">
                        <a href="${mvc.basePath}/models" class="btn btn-outline-secondary">Torna a l'explorador</a>
                    </div>
                </div>
            </div>
        </div>

    </body>
</html>