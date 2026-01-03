<%-- 
    Document   : login
    Created on : 3 ene 2026, 11:53:41
    Author     : bllad
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <link href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container" style="margin-top: 50px;">
        <div class="col-md-6 col-md-offset-3">
            <div class="panel panel-default">
                <div class="panel-heading">Please Sign In</div>
                <div class="panel-body">
                    <c:if test="${not empty loginError}">
                        <div class="alert alert-danger">${loginError}</div>
                    </c:if>
                    
                    <form action="${pageContext.request.contextPath}/Web/login" method="POST">
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