<%-- 
    Document   : model-detail
    Created on : 3 ene 2026, 11:51:40
    Author     : bllad
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${model.name} - Details</title>
    <link href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <nav class="navbar navbar-default">
        <div class="container">
            <div class="navbar-header">
                <a class="navbar-brand" href="${pageContext.request.contextPath}/Web/models">Back to Catalog</a>
            </div>
        </div>
    </nav>

    <div class="container">
        <div class="row">
            <div class="col-md-8 col-md-offset-2">
                <div class="panel panel-primary">
                    <div class="panel-heading">
                        <h3 class="panel-title">${model.name} <small style="color: white;">(${model.provider})</small></h3>
                    </div>
                    <div class="panel-body">
                        <div class="row">
                            <div class="col-md-4 text-center">
                                <c:choose>
                                    <c:when test="${not empty model.logo}">
                                        <img src="${model.logo}" class="img-responsive img-thumbnail" alt="Logo">
                                    </c:when>
                                    <c:otherwise>
                                        <div class="well">No Logo</div>
                                    </c:otherwise>
                                </c:choose>
                                <br>
                                <span class="label label-success">${model.mainCapability}</span>
                                <c:if test="${model.private}">
                                    <span class="label label-danger">Private</span>
                                </c:if>
                            </div>
                            <div class="col-md-8">
                                <p><strong>Description:</strong> ${model.description}</p>
                                <p><strong>Summary:</strong> ${model.summary}</p>
                                <hr>
                                <dl class="dl-horizontal">
                                    <dt>Version:</dt> <dd>${model.version}</dd>
                                    <dt>Context Length:</dt> <dd>${model.contextLength}</dd>
                                    <dt>Quality Index:</dt> <dd>${model.qualityIndex}</dd>
                                    <dt>Release Date:</dt> <dd>${model.trainingDataDate}</dd>
                                    <dt>License:</dt> <dd>${model.license}</dd>
                                </dl>
                            </div>
                        </div>
                    </div>
                    <div class="panel-footer text-right">
                        <a href="${pageContext.request.contextPath}/Web/models" class="btn btn-default">Close</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <%@include file="layout/footer.jsp" %>
</body>
</html>
