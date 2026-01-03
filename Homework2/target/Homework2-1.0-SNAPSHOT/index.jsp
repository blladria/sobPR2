<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    // Redirige al path configurado en JakartaRestConfiguration ("mvc") + el controlador ("models")
    response.sendRedirect(request.getContextPath() + "/mvc/models");
%>