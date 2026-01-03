<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%-- SOLUCIÓN ERROR 500: Eliminada la taglib 'mvc' que no se usaba y rompía la compilación --%>

<!DOCTYPE html>
<html>
    <head>
        <title>Sign Up - AI Catalog</title>
        <link href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <%@include file="layout/navbar.jsp" %>

        <div class="container mt-5">
            <div class="row justify-content-center">
                <div class="col-md-6">
                    <div class="card shadow">
                        <div class="card-header bg-primary text-white">
                            <h4 class="mb-0">Sign Up</h4>
                        </div>
                        <div class="card-body">

                            <%-- Mensajes de error --%>
                            <c:if test="${not empty globalError}">
                                <div class="alert alert-danger">${globalError}</div>
                            </c:if>
                            <c:if test="${not empty errors}">
                                <div class="alert alert-warning">
                                    <ul>
                                        <c:forEach var="error" items="${errors}">
                                            <li>${error.message}</li>
                                            </c:forEach>
                                    </ul>
                                </div>
                            </c:if>

                            <form action="${pageContext.request.contextPath}/Web/SignUp" method="post">
                                <div class="mb-3">
                                    <label for="username" class="form-label">Username</label>
                                    <input type="text" class="form-control" id="username" name="username" value="${userForm.username}" required>
                                </div>
                                <div class="mb-3">
                                    <label for="name" class="form-label">Full Name</label>
                                    <input type="text" class="form-control" id="name" name="name" value="${userForm.name}" required>
                                </div>
                                <div class="mb-3">
                                    <label for="email" class="form-label">Email</label>
                                    <input type="email" class="form-control" id="email" name="email" value="${userForm.email}" required>
                                </div>
                                <div class="mb-3">
                                    <label for="password" class="form-label">Password</label>
                                    <input type="password" class="form-control" id="password" name="password" required>
                                </div>

                                <div class="d-grid gap-2">
                                    <button type="submit" class="btn btn-primary">Create Account</button>
                                    <a href="${pageContext.request.contextPath}/Web/models" class="btn btn-secondary">Back to Home</a>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <%@include file="layout/footer.jsp" %>
    </body>
</html>