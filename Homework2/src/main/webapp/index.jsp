<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    // CORREGIDO: Ahora apunta a "Web" en lugar de "mvc"
    response.sendRedirect(request.getContextPath() + "/Web/models");
%>