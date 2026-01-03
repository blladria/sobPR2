<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>AI Models List</title>
    <link href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
    <style>
        /* Estilo para evitar el "salto" de imágenes */
        .card-img-top {
            width: 100%;
            height: 200px; /* Altura fija */
            object-fit: cover; /* Recorta la imagen para que encaje bien */
            background-color: #f0f0f0; /* Color de fondo mientras carga */
        }
        .model-card {
            transition: transform 0.2s;
        }
        .model-card:hover {
            transform: scale(1.02);
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        }
    </style>
</head>
<body>
    
    <%@include file="layout/navbar.jsp" %>
    
    <%@include file="layout/alert.jsp" %>

    <div class="container mt-4">
        <h2>Available Models</h2>
        
        <form action="${pageContext.request.contextPath}/mvc/models" method="GET" class="mb-4 p-3 bg-light rounded">
            <div class="row">
                <div class="col-md-4">
                    <input type="text" name="capability" class="form-control" placeholder="Filter by Capability (e.g. text-generation)">
                </div>
                <div class="col-md-4">
                    <input type="text" name="provider" class="form-control" placeholder="Filter by Provider (e.g. OpenAI)">
                </div>
                <div class="col-md-4">
                    <button type="submit" class="btn btn-primary btn-block">Filter</button>
                    <a href="${pageContext.request.contextPath}/mvc/models" class="btn btn-secondary">Reset</a>
                </div>
            </div>
        </form>

        <div class="row">
            <c:forEach items="${models}" var="model">
                <div class="col-md-4 mb-4">
                    <div class="card model-card h-100">
                        <img src="${model.image}" 
                             class="card-img-top" 
                             alt="${model.name}"
                             onerror="this.src='${pageContext.request.contextPath}/resources/img/mistral.png';"> 
                        
                        <div class="card-body d-flex flex-column">
                            <h5 class="card-title">${model.name}</h5>
                            <h6 class="card-subtitle mb-2 text-muted">${model.provider}</h6>
                            <p class="card-text text-truncate">${model.description}</p>
                            
                            <div class="mt-auto">
                                <a href="${pageContext.request.contextPath}/mvc/models/${model.name}" class="btn btn-info btn-sm">View Details</a>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>

    <%@include file="layout/footer.jsp" %>

    <script src="${pageContext.request.contextPath}/resources/js/jquery-1.11.1.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/bootstrap.min.js"></script>
</body>
</html>