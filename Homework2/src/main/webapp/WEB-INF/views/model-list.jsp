<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Model List - Homework 2</title>

        <link href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">

        <style>
            /* Ajustes adicionales para que las cards se vean bien */
            .card {
                background: #fff;
                margin-bottom: 20px;
                position: relative;
                display: flex;
                flex-direction: column;
                min-width: 0;
                word-wrap: break-word;
                background-clip: border-box;
                border: 1px solid rgba(0,0,0,.125);
                border-radius: .25rem;
            }
            .card-body {
                flex: 1 1 auto;
                padding: 1.25rem;
            }
            .card-footer {
                padding: 0.75rem 1.25rem;
                background-color: rgba(0,0,0,.03);
                border-top: 1px solid rgba(0,0,0,.125);
            }
            .shadow-sm {
                box-shadow: 0 .125rem .25rem rgba(0,0,0,.075)!important;
            }
            .h-100 {
                height: 100%!important;
            }

            /* Ajuste para separar el contenido del navbar */
            body {
                padding-top: 70px;
                padding-bottom: 30px;
            }
        </style>
    </head>

    <body>
        <jsp:include page="layout/navbar.jsp" />

        <div class="container">

            <jsp:include page="layout/alert.jsp" />

            <div class="row">
                <div class="col-md-3">
                    <div class="panel panel-default shadow-sm">
                        <div class="panel-heading">
                            <h3 class="panel-title"><i class="glyphicon glyphicon-filter"></i> Filters</h3>
                        </div>
                        <div class="panel-body">
                            <form action="${pageContext.request.contextPath}/Web/models" method="GET">

                                <div class="form-group">
                                    <label for="capability">Capability</label>
                                    <select name="capability" id="capability" class="form-control">
                                        <option value="">All Capabilities</option>
                                        <c:forEach items="${uniqueCapabilities}" var="cap">
                                            <option value="${cap}" ${param.capability == cap ? 'selected' : ''}>${cap}</option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="form-group">
                                    <label for="provider">Provider</label>
                                    <select name="provider" id="provider" class="form-control">
                                        <option value="">All Providers</option>
                                        <c:forEach items="${uniqueProviders}" var="prov">
                                            <option value="${prov}" ${param.provider == prov ? 'selected' : ''}>${prov}</option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <button type="submit" class="btn btn-primary btn-block">
                                    Apply Filters
                                </button>
                                <a href="${pageContext.request.contextPath}/Web/models" class="btn btn-default btn-block">Clear</a>
                            </form>
                        </div>
                    </div>
                </div>

                <div class="col-md-9">
                    <h2 style="margin-top:0; border-bottom: 1px solid #eee; padding-bottom: 10px; margin-bottom: 20px;">Available Models</h2>

                    <div class="row">
                        <c:forEach items="${models}" var="model">
                            <div class="col-md-4 col-sm-6">
                                <div class="card h-100 shadow-sm">
                                    <div class="card-body">
                                        <div style="display: flex; align-items: center; margin-bottom: 15px;">
                                            <div style="width: 50px; height: 50px; background: #f8f9fa; border-radius: 50%; border: 1px solid #ddd; display: flex; align-items: center; justify-content: center; margin-right: 15px; overflow: hidden;">
                                                <c:choose>
                                                    <c:when test="${not empty model.logo}">
                                                        <img src="${model.logo}" alt="logo" style="max-width: 100%; max-height: 100%;">
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span style="font-size: 18px; font-weight: bold; color: #555;">${model.provider.substring(0,1)}</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                            <div>
                                                <h4 style="margin: 0; font-size: 16px; font-weight: bold;">${model.name}</h4>
                                                <small class="text-muted">${model.provider}</small>
                                            </div>
                                        </div>

                                        <p style="font-size: 13px; color: #666; height: 60px; overflow: hidden;">
                                            ${model.description}
                                        </p>

                                        <div style="margin-top: 10px;">
                                            <span class="label label-info">${model.mainCapability}</span>
                                            <c:if test="${model.private}">
                                                <span class="label label-warning"><i class="glyphicon glyphicon-lock"></i> Private</span>
                                            </c:if>
                                        </div>
                                    </div>
                                    <div class="card-footer bg-white text-center">
                                        <a href="${pageContext.request.contextPath}/Web/models/detail?id=${model.id}" class="btn btn-primary btn-sm btn-block">View Details</a>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>

                        <c:if test="${empty models}">
                            <div class="col-12">
                                <div class="alert alert-info">No models found matching your criteria.</div>
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>

        <jsp:include page="layout/footer.jsp" />

        <script src="${pageContext.request.contextPath}/resources/js/jquery-1.11.1.min.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/bootstrap.min.js"></script>
    </body>
</html>