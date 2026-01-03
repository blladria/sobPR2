<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <link href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <nav class="navbar navbar-default navbar-static-top">
        <div class="container">
            <div class="navbar-header">
                <a class="navbar-brand" href="${pageContext.request.contextPath}/Web/models">
                     <img src="${pageContext.request.contextPath}/resources/img/ETSEcentrat.png" alt="Logo" height="30" style="display:inline-block;">
                     Models Catalog
                </a>
            </div>
            <div id="navbar" class="navbar-collapse collapse">
                <ul class="nav navbar-nav navbar-right">
                    <li class="active"><a href="${pageContext.request.contextPath}/Web/login">Login</a></li>
                    <li><a href="${pageContext.request.contextPath}/Web/SignUp">Sign Up</a></li>
                </ul>
            </div>
        </div>
    </nav>
    <div class="container" style="margin-top: 50px;">
        <div class="col-md-6 col-md-offset-3">
            <div class="panel panel-default">
                <div class="panel-heading">Please Sign In</div>
                <div class="panel-body">
                    <c:if test="${not empty loginError}">
                        <div class="alert alert-danger">${loginError}</div>
                    </c:if>
                    
                    <form action="${pageContext.request.contextPath}/Web/login" method="POST">
                        
                        <c:set var="retUrl" value="${not empty returnUrl ? returnUrl : loginForm.returnUrl}" />
                        <input type="hidden" name="returnUrl" value="${retUrl}">
                        
                        <div class="form-group">
                            <label>Username</label>
                            <input type="text" name="username" class="form-control" value="${loginForm.username}" required autofocus>
                        </div>
                        <div class="form-group">
                            <label>Password</label>
                            <input type="password" name="password" class="form-control" required>
                        </div>
                        <button type="submit" class="btn btn-primary btn-block">Login</button>
                    </form>
                </div>
                <div class="panel-footer">
                    Don't have an account? <a href="${pageContext.request.contextPath}/Web/SignUp">Sign Up</a>
                </div>
            </div>
        </div>
    </div>
</body>
</html>