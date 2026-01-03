<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<jsp:include page="layout/header.jsp" />

<div class="container" style="margin-top: 30px;">

    <%-- Botón volver --%>
    <div class="row">
        <div class="col-md-12">
            <a href="${pageContext.request.contextPath}/models" class="btn btn-default">
                &larr; Back to List
            </a>
        </div>
    </div>

    <%-- Cabecera del Modelo --%>
    <div class="row" style="margin-top: 20px;">
        <div class="col-md-12">
            <h1 class="page-header">
                ${model.name} 
                <small>by ${model.provider}</small>

                <%-- Candado si es privado --%>
                <c:if test="${model.private}">
                    <span class="pull-right" style="font-size: 0.6em; color: #d9534f;" title="Private Model">
                        <span class="glyphicon glyphicon-lock"></span> Private
                    </span>
                </c:if>
            </h1>
        </div>
    </div>

    <div class="row">
        <%-- Columna Izquierda: Descripción y Tags --%>
        <div class="col-md-8">
            <h3>Description</h3>
            <p style="font-size: 1.1em; line-height: 1.6;">
                <c:choose>
                    <c:when test="${not empty model.description}">
                        ${model.description}
                    </c:when>
                    <c:otherwise>
                        No description provided for this model.
                    </c:otherwise>
                </c:choose>
            </p>

            <hr>

            <h4>Capabilities</h4>
            <div style="margin-top: 10px;">
                <%-- Main Capability destacado --%>
                <span class="label label-primary" style="font-size: 100%; margin-right: 5px;">
                    ${model.mainCapability}
                </span>

                <%-- Resto de capabilities --%>
                <c:forEach items="${model.capabilities}" var="cap">
                    <span class="label label-info" style="font-size: 90%; margin-right: 5px;">
                        ${cap}
                    </span>
                </c:forEach>
            </div>
        </div>

        <%-- Columna Derecha: Tabla de Especificaciones --%>
        <div class="col-md-4">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">Technical Specifications</h3>
                </div>
                <table class="table">
                    <tr>
                        <th>Version</th>
                        <td>${not empty model.version ? model.version : '-'}</td>
                    </tr>
                    <tr>
                        <th>Context Window</th>
                        <td>${not empty model.contextWindow ? model.contextWindow : '-'}</td>
                    </tr>
                    <tr>
                        <th>Quality Index</th>
                        <td>
                            <c:if test="${not empty model.qualityIndex}">
                                <div class="progress" style="margin-bottom: 0;">
                                    <div class="progress-bar progress-bar-success" role="progressbar" 
                                         aria-valuenow="${model.qualityIndex}" aria-valuemin="0" aria-valuemax="100" 
                                         style="width: ${model.qualityIndex}%;">
                                        ${model.qualityIndex}/100
                                    </div>
                                </div>
                            </c:if>
                            <c:if test="${empty model.qualityIndex}">-</c:if>
                            </td>
                        </tr>
                        <tr>
                            <th>License</th>
                            <td>${not empty model.license ? model.license : '-'}</td>
                    </tr>
                    <tr>
                        <th>Last Updated</th>
                        <td>${not empty model.lastUpdated ? model.lastUpdated : '-'}</td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</div>

<jsp:include page="layout/footer.jsp" />