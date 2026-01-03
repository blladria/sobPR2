<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ca">
    <head>
        <meta charset="UTF-8">
        <title>Perfil - AI Market</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            body {
                background-color: #f8f9fa;
            }
            .profile-card {
                max-width: 600px;
                margin: 50px auto;
                background: white;
                padding: 30px;
                border-radius: 15px;
                box-shadow: 0 4px 15px rgba(0,0,0,0.1);
            }
            .avatar {
                width: 80px;
                height: 80px;
                background: #0d6efd;
                color: white;
                border-radius: 50%;
                display: flex;
                align-items: center;
                justify-content: center;
                font-size: 2rem;
                margin: 0 auto 20px;
            }
        </style>
    </head>
    <body>
        <nav class="navbar navbar-dark bg-dark mb-4">
            <div class="container">
                <a class="navbar-brand" href="${mvc.basePath}/models">SOB AI Market</a>
                <div class="ms-auto">
                    <a href="${mvc.basePath}/logout" class="btn btn-outline-light btn-sm">Cerrar Sesión</a>
                </div>
            </div>
        </nav>

        <div class="container">
            <div class="profile-card text-center shadow">
                <div class="avatar">${userProfile.username.substring(0,1).toUpperCase()}</div>
                <h2 class="fw-bold">@<c:out value="${userProfile.username}"/></h2>
                <p class="text-muted"><c:out value="${userProfile.email}"/></p>
                <hr>
                <div class="text-start mb-4">
                    <label class="text-muted small fw-bold">NOMBRE COMPLETO</label>
                    <p class="fs-5"><c:out value="${userProfile.name}"/></p>
                </div>

                <c:if test="${not empty lastModel}">
                    <div class="card bg-light border-0 text-start p-3 mb-3 border-start border-primary border-4">
                        <h6 class="text-primary fw-bold small">ÚLTIMO MODELO CONSULTADO</h6>
                        <div class="d-flex align-items-center">
                            <img src="${lastModel.logo}" width="40" height="40" class="me-3 object-fit-contain">
                            <div>
                                <a href="${mvc.basePath}/models/${lastModel.id}" class="text-decoration-none fw-bold text-dark">${lastModel.name}</a>
                                <br><span class="badge bg-secondary small">${lastModel.mainCapability}</span>
                            </div>
                        </div>
                    </div>
                </c:if>

                <a href="${mvc.basePath}/models" class="btn btn-primary w-100 mt-3">Volver a la Tienda</a>
            </div>
        </div>
    </body>
</html>