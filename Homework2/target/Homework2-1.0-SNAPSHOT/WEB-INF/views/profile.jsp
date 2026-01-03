<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>User Profile</title>
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
                        <li>
                            <p class="navbar-text">
                                Benvingut <a href="${pageContext.request.contextPath}/Web/profile" class="navbar-link" style="font-weight:bold; text-decoration: underline;">${sessionScope.user.username}</a>!
                            </p>
                        </li>
                        <li><a href="${pageContext.request.contextPath}/Web/logout">Logout</a></li>
                    </ul>
                </div>
            </div>
        </nav>

        <div class="container" style="margin-top: 30px;">
            <div class="row">
                <div class="col-md-6 col-md-offset-3">
                    <div class="panel panel-info">
                        <div class="panel-heading">
                            <h3 class="panel-title"><span class="glyphicon glyphicon-user"></span> User Profile</h3>
                        </div>
                        <div class="panel-body">
                            <div class="row">
                                <div class="col-md-3" align="center"> 
                                    <img alt="User Pic" src="https://ui-avatars.com/api/?name=${user.username}&background=random&size=128" class="img-circle img-responsive"> 
                                </div>
                                <div class="col-md-9">
                                    <table class="table table-user-information">
                                        <tbody>
                                            <tr>
                                                <td><strong>Username:</strong></td>
                                                <td>${user.username}</td>
                                            </tr>
                                            <tr>
                                                <td><strong>Full Name:</strong></td>
                                                <td>${user.name}</td>
                                            </tr>
                                            <tr>
                                                <td><strong>Email:</strong></td>
                                                <td>${user.email}</td>
                                            </tr>
                                        </tbody>
                                    </table>
                                    <a href="${pageContext.request.contextPath}/Web/models" class="btn btn-primary">Back to Models</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <%@include file="layout/footer.jsp" %>
    </body>
</html>