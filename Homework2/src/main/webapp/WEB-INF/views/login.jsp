<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Sign In - Homework 2</title>

        <link href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
        <style>body {
            padding-top: 70px;
            padding-bottom: 30px;
        }</style>
    </head>

    <body>
        <jsp:include page="layout/navbar.jsp" />

        <div class="container">

            <jsp:include page="layout/alert.jsp" />

            <div class="row">
                <div class="col-md-4 col-md-offset-4">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h3 class="panel-title">Please Sign In</h3>
                        </div>
                        <div class="panel-body">

                            <c:if test="${not empty error}">
                                <div class="alert alert-danger">${error}</div>
                            </c:if>

                            <form method="POST" action="${pageContext.request.contextPath}/mvc/login">

                                <c:if test="${not empty returnUrl}">
                                    <input type="hidden" name="returnUrl" value="${returnUrl}" />
                                </c:if>

                                <div class="form-group">
                                    <label for="username">Username</label>
                                    <input type="text" class="form-control" id="username" name="username" 
                                           placeholder="Username" required autofocus value="${username}">
                                </div>

                                <div class="form-group">
                                    <label for="password">Password</label>
                                    <input type="password" class="form-control" id="password" name="password" 
                                           placeholder="Password" required>
                                </div>

                                <button type="submit" class="btn btn-primary btn-block">Sign In</button>
                            </form>
                        </div>
                        <div class="panel-footer text-center">
                            Don't have an account? <a href="${pageContext.request.contextPath}/mvc/signup">Sign up</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <jsp:include page="layout/footer.jsp" />

        <script src="${pageContext.request.contextPath}/resources/js/jquery-1.11.1.min.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/bootstrap.min.js"></script>
    </body>
</html>