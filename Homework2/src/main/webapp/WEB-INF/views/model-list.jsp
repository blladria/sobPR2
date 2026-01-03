<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>AI Models Catalog</title>
        <link href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
        <style>
            .card-img-top {
                width: 100%;
                height: 150px;
                object-fit: contain;
                padding: 10px;
                background-color: #f8f9fa;
            }
            .card {
                height: 100%;
                transition: transform 0.2s;
            }
            .card:hover {
                transform: scale(1.02);
                box-shadow: 0 4px 8px rgba(0,0,0,0.1);
            }
            .private-icon {
                position: absolute;
                top: 10px;
                right: 10px;
                color: #dc3545;
                font-size: 1.2rem;
                z-index: 100;
                background: rgba(255,255,255,0.7);
                border-radius: 50%;
                padding: 5px;
            }
        </style>
    </head>
    <body>
        <nav class="navbar navbar-default navbar-static-top">
            <div class="container">
                <div class="navbar-header">
                    <a class="navbar-brand" href="${pageContext.request.contextPath}/Web/models">
                        <img src="${pageContext.request.contextPath}/resources/img/ETSEcentrat.png" alt="Logo" height="30">
                    </a>
                    <a class="navbar-brand" href="${pageContext.request.contextPath}/Web/models">Models Catalog</a>
                </div>
                <div id="navbar" class="navbar-collapse collapse">
                    <ul class="nav navbar-nav navbar-right">
                        <c:choose>
                            <c:when test="${not empty sessionScope.user}">
                                <li><p class="navbar-text">Benvingut <strong>${sessionScope.user.username}</strong>!</p></li>
                                <li><a href="${pageContext.request.contextPath}/Web/logout">Logout</a></li>
                                </c:when>
                                <c:otherwise>
                                <li><a href="${pageContext.request.contextPath}/Web/login">Login</a></li>
                                <li><a href="${pageContext.request.contextPath}/Web/SignUp">Sign Up</a></li>
                                </c:otherwise>
                            </c:choose>
                    </ul>
                </div>
            </div>
        </nav>

        <div class="container">
            <div class="well">
                <form action="${pageContext.request.contextPath}/Web/models" method="GET" class="form-inline">

                    <div class="form-group">
                        <label for="provider">Provider:</label>
                        <select name="provider" class="form-control">
                            <option value="">All Providers</option>
                            <option value="OpenAI" ${param.provider == 'OpenAI' ? 'selected' : ''}>OpenAI</option>
                            <option value="Anthropic" ${param.provider == 'Anthropic' ? 'selected' : ''}>Anthropic</option>
                            <option value="Google" ${param.provider == 'Google' ? 'selected' : ''}>Google</option>
                            <option value="Meta" ${param.provider == 'Meta' ? 'selected' : ''}>Meta</option>
                        </select>
                    </div>

                    <div class="form-group" style="margin-left: 15px;">
                        <label for="capability">Capability:</label>
                        <select name="capability" class="form-control">
                            <option value="">All Capabilities</option>

                            <option value="Chat Completion" ${param.capability == 'Chat Completion' ? 'selected' : ''}>Chat Completion</option>
                            <option value="Image Generation" ${param.capability == 'Image Generation' ? 'selected' : ''}>Image Generation</option>
                            <option value="Code Generation" ${param.capability == 'Code Generation' ? 'selected' : ''}>Code Generation</option>

                        </select>
                    </div>

                    <button type="submit" class="btn btn-primary pull-right">Filter</button>
                </form>
            </div>

            <div class="row">
                <c:forEach items="${modelList}" var="model">
                    <div class="col-md-4 col-sm-6" style="margin-bottom: 20px;">
                        <div class="card panel panel-default">
                            <div class="panel-body" style="position: relative;">

                                <c:if test="${model.isPrivate()}">
                                    <span class="glyphicon glyphicon-lock private-icon" title="Private Model"></span>
                                </c:if>

                                <div class="text-center">
                                    <c:choose>
                                        <c:when test="${not empty model.logo}">
                                            <img src="${model.logo}" class="card-img-top" alt="${model.provider} logo">
                                        </c:when>
                                        <c:otherwise>
                                            <div style="height: 150px; line-height: 150px; background: #eee;">No Logo</div>
                                        </c:otherwise>
                                    </c:choose>
                                </div>

                                <hr>

                                <h4>
                                    <a href="${pageContext.request.contextPath}/Web/models/${model.id}">
                                        ${model.name}
                                    </a>
                                </h4>
                                <p><strong>Provider:</strong> ${model.provider}</p>
                                <p><strong>Capability:</strong> <span class="label label-info">${model.mainCapability}</span></p>
                                <p style="min-height: 60px;">
                                    ${model.summary}
                                </p>
                            </div>
                        </div>
                    </div>
                </c:forEach>

                <c:if test="${empty modelList}">
                    <div class="alert alert-warning">
                        No models found matching your criteria.
                    </div>
                </c:if>
            </div>
        </div>

        <%@include file="layout/footer.jsp" %>
    </body>
</html>