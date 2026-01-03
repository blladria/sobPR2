<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container-fluid">
        <%-- SOLUCIÓN ESTÉTICA: Eliminado el texto "AI Catalog", solo queda la imagen --%>
        <a class="navbar-brand" href="${pageContext.request.contextPath}/Web/models">
            <img src="${pageContext.request.contextPath}/resources/img/ETSEcentrat.png" alt="Logo" width="30" height="24" class="d-inline-block align-text-top">
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>
       
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav me-auto">
            </ul>
            
            <%-- SOLUCIÓN ESTÉTICA: Aseguramos list-style: none con list-unstyled o inline style --%>
            <ul class="navbar-nav ms-auto align-items-center list-unstyled" style="margin-bottom: 0;">
                <c:choose>
                    <c:when test="${not empty sessionScope.user}">
                         <li class="nav-item me-3">
                             <span class="nav-link text-light">Welcome, <a href="${pageContext.request.contextPath}/Web/profile" class="text-info text-decoration-none fw-bold">${sessionScope.user.username}</a>!</span>
                        </li>
     
                        <li class="nav-item">
                            <a class="btn btn-danger btn-sm" href="${pageContext.request.contextPath}/Web/logout">Logout</a>
                        </li>
                    </c:when>
    
                    <c:otherwise>
                        <li class="nav-item me-2">
                             <a class="btn btn-success btn-sm" href="${pageContext.request.contextPath}/Web/login">Login</a>
                        </li>
 
                        <li class="nav-item">
                           <a class="btn btn-primary btn-sm" href="${pageContext.request.contextPath}/Web/SignUp">Sign Up</a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>