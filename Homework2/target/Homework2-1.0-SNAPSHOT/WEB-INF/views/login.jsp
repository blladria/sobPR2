<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <title>Login - AI Models</title>
    <link href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <%@ include file="layout/navbar.jsp" %>

    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-4">
                <div class="card shadow">
                    <div class="card-header bg-dark text-white text-center">
                        <h4>Login with your Account</h4>
                    </div>
                    <div class="card-body">
                        <c:if test="${not empty loginError}">
                            <div class="alert alert-danger">${loginError}</div>
                        </c:if>
                        <c:if test="${param.registered}">
                            <div class="alert alert-success">Registro completado. Por favor, inicia sesión.</div>
                        </c:if>

                        <form action="login" method="post">
                            <input type="hidden" name="returnUrl" value="${returnUrl}"/>
                            
                            <div class="mb-3">
                                <label class="form-label">Usuario</label>
                                <input type="text" name="username" class="form-control" placeholder="User Name" required autofocus>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Contraseña</label>
                                <input type="password" name="password" class="form-control" placeholder="Password" required>
                            </div>
                            <div class="mb-3 form-check">
                                <input type="checkbox" class="form-check-input" id="remember">
                                <label class="form-check-label" for="remember">Remember me</label>
                            </div>
                            <button type="submit" class="btn btn-dark w-100">LOGIN NOW</button>
                        </form>
                    </div>
                    <div class="card-footer text-center">
                        <small>¿No tienes cuenta? <a href="${pageContext.request.contextPath}/SignUp">Regístrate aquí</a></small>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>