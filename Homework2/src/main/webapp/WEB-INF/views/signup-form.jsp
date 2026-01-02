<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <title>Sign Up</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css" />
    <script src="<c:url value="/resources/js/jquery-1.11.1.min.js" />"></script>
    <script src="<c:url value="/resources/js/bootstrap.min.js" />"></script>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"/>
    
    <link href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css"
          rel="stylesheet"
          integrity="sha384-wvfXpqpZZVQGK6TAh5PVlGOfQNHSoD2xbE+QkPxCAFlNEevoEH3Sl0sibVcOQVnN"
          crossorigin="anonymous"/>
</head>
<body>
    <div class="container">
        <div class="col-md-offset-2 col-md-7">
            <h2 class="text-center">Homework 2 - Registro</h2>
            <div class="panel panel-info">
                <div class="panel-heading">
                    <div class="panel-title">Sign Up</div>
                </div>
                <div class="panel-body">
                    
                    <form action="${pageContext.request.contextPath}/Web/SignUp" method="POST" class="form-horizontal" role="form">
                        
                        <input type="hidden" name="${mvc.csrf.name}" value="${mvc.csrf.token}"/>

                        <div class="form-group">
                            <label for="username" class="col-md-3 control-label">Username</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" name="username" placeholder="Choose a username" value="${user.username}">
                                <c:if test="${not empty bindingResult.getErrors('username')}">
                                    <div class="text-danger">
                                        <c:forEach var="error" items="${bindingResult.getErrors('username')}">
                                            ${error.message}<br/>
                                        </c:forEach>
                                    </div>
                                </c:if>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="password" class="col-md-3 control-label">Password</label>
                            <div class="col-md-9">
                                <input type="password" class="form-control" name="password" placeholder="Password">
                                <c:if test="${not empty bindingResult.getErrors('password')}">
                                    <div class="text-danger">
                                        <c:forEach var="error" items="${bindingResult.getErrors('password')}">
                                            ${error.message}<br/>
                                        </c:forEach>
                                    </div>
                                </c:if>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="name" class="col-md-3 control-label">Full Name</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" name="name" placeholder="First and Last Name" value="${user.name}">
                                <c:if test="${not empty bindingResult.getErrors('name')}">
                                    <div class="text-danger">
                                        <c:forEach var="error" items="${bindingResult.getErrors('name')}">
                                            ${error.message}<br/>
                                        </c:forEach>
                                    </div>
                                </c:if>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="email" class="col-md-3 control-label">Email</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" name="email" placeholder="Email Address" value="${user.email}">
                                <c:if test="${not empty bindingResult.getErrors('email')}">
                                    <div class="text-danger">
                                        <c:forEach var="error" items="${bindingResult.getErrors('email')}">
                                            ${error.message}<br/>
                                        </c:forEach>
                                    </div>
                                </c:if>
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-md-offset-3 col-md-9">
                                <button type="submit" class="btn btn-info"><i class="fa fa-user-plus"></i> Sign Up</button>
                            </div>
                        </div>
                        
                    </form>
                    
                    <c:if test="${not empty message}">
                         <div class="alert alert-danger">
                             ${message}
                         </div>
                    </c:if>

                    <c:if test="${attempts.hasExceededMaxAttempts()}">
                        <div class="alert alert-danger text-center">
                            <img class="mb-4" src="<c:url value="/resources/img/Invalid.png" />" alt="" width="50" />
                            <br/>
                            <strong>Too many attempts!</strong> Please try again later.
                        </div>
                    </c:if>
                    
                    <jsp:include page="/WEB-INF/views/layout/alert.jsp" />
                </div>
            </div>
        </div>
    </div>
    <jsp:include page="/WEB-INF/views/layout/footer.jsp" />
</body>
</html>