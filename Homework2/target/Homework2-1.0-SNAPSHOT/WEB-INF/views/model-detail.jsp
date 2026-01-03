<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${model.name} - Details</title>
        <link href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
        <style>
            /* Estilos para igualar la Figura 2 del PDF */
            .model-header {
                display: flex;
                align-items: flex-start;
                margin-bottom: 20px;
                border-bottom: 1px solid #eee;
                padding-bottom: 20px;
            }
            .model-logo {
                width: 80px;
                height: 80px;
                object-fit: contain;
                border-radius: 10px;
                margin-right: 20px;
                border: 1px solid #ddd;
                padding: 5px;
            }
            .model-title {
                margin-top: 0;
                font-weight: bold;
                font-size: 2.5em;
                display: flex;
                align-items: center;
            }
            .version-badge {
                font-size: 0.4em;
                background-color: #eef;
                color: #555;
                border: 1px solid #ccf;
                border-radius: 15px;
                padding: 3px 8px;
                margin-left: 15px;
                vertical-align: middle;
            }
            .provider-info {
                color: #777;
                font-size: 1.1em;
            }
            .capability-badge {
                display: inline-block;
                border: 1px solid #5bc0de;
                color: #31708f;
                background-color: #eef9fd;
                padding: 5px 15px;
                border-radius: 20px;
                margin-right: 5px;
                margin-bottom: 5px;
                font-weight: bold;
            }
            .spec-table td {
                padding: 8px 0;
                font-size: 1.1em;
            }
            .spec-label {
                color: #555;
            }
            .spec-value {
                text-align: right;
                font-weight: bold;
                color: #333;
            }
        </style>
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

        <div class="container" style="margin-top: 20px;">
            
            <a href="${pageContext.request.contextPath}/Web/models" class="btn btn-default" style="margin-bottom: 20px;">&larr; Back to List</a>

            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>

            <c:if test="${not empty model}">
                <div class="panel panel-default">
                    <div class="panel-body">
                        
                        <div class="model-header">
                            <c:choose>
                                <c:when test="${not empty model.logo}">
                                    <img src="${model.logo}" class="model-logo" alt="Logo">
                                </c:when>
                                <c:otherwise>
                                    <div class="model-logo" style="display:flex;align-items:center;justify-content:center;background:#eee;">No Logo</div>
                                </c:otherwise>
                            </c:choose>
                            
                            <div>
                                <h1 class="model-title">
                                    ${model.name}
                                    <span class="version-badge">Version: ${model.version}</span>
                                </h1>
                                <div class="provider-info">
                                    ${model.provider} &bull; Last updated ${model.lastUpdated}
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-12">
                                <p style="font-size: 1.1em; line-height: 1.6;">
                                    ${model.description}
                                </p>
                            </div>
                        </div>

                        <div class="row" style="margin-top: 15px; margin-bottom: 30px;">
                            <div class="col-md-12">
                                <c:forEach items="${model.capabilities}" var="cap">
                                    <span class="capability-badge">${cap}</span>
                                </c:forEach>
                            </div>
                        </div>

                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <h3 class="panel-title" style="font-weight:bold; font-size:1.5em;">Model Specifications</h3>
                            </div>
                            <div class="panel-body">
                                <table class="table" style="margin-bottom: 0;">
                                    <tbody>
                                        <tr>
                                            <td class="spec-label">Context Length</td>
                                            <td class="spec-value">${model.contextLength}</td>
                                        </tr>
                                        <tr>
                                            <td class="spec-label">Quality Index</td>
                                            <td class="spec-value">${model.qualityIndex}</td>
                                        </tr>
                                        <tr>
                                            <td class="spec-label">License</td>
                                            <td class="spec-value">${model.license}</td>
                                        </tr>
                                        <tr>
                                            <td class="spec-label">Training Data</td>
                                            <td class="spec-value">${model.trainingDataDate}</td>
                                        </tr>
                                        <tr>
                                            <td class="spec-label">Last Updated</td>
                                            <td class="spec-value">${model.lastUpdated}</td>
                                        </tr>
                                        <tr>
                                            <td class="spec-label">Input Type</td>
                                            <td class="spec-value">${model.inputType}</td>
                                        </tr>
                                        <tr>
                                            <td class="spec-label">Output Type</td>
                                            <td class="spec-value">${model.outputType}</td>
                                        </tr>
                                        <tr>
                                            <td class="spec-label">Publisher</td>
                                            <td class="spec-value">${model.publisher}</td>
                                        </tr>
                                        <tr>
                                            <td class="spec-label">Languages</td>
                                            <td class="spec-value">${model.languages}</td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>

                    </div>
                </div>
            </c:if>
        </div>

        <%@include file="layout/footer.jsp" %>
    </body>
</html>