package org.apache.jsp;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.jsp.*;
import java.sql.*;

public final class install_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List<String> _jspx_dependants;

  private org.glassfish.jsp.api.ResourceInjector _jspx_resourceInjector;

  public java.util.List<String> getDependants() {
    return _jspx_dependants;
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;

    try {
      response.setContentType("text/html;charset=UTF-8");
      response.setHeader("X-Powered-By", "JSP/2.3");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;
      _jspx_resourceInjector = (org.glassfish.jsp.api.ResourceInjector) application.getAttribute("com.sun.appserv.jsp.resource.injector");

      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("<!DOCTYPE html>\n");
      out.write("<html>\n");
      out.write("    <head>\n");
      out.write("        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n");
      out.write("        <title>Database SQL Load</title>\n");
      out.write("    </head>\n");
      out.write("    <style>\n");
      out.write("        .error {\n");
      out.write("            color: red;\n");
      out.write("        }\n");
      out.write("        pre {\n");
      out.write("            color: green;\n");
      out.write("        }\n");
      out.write("    </style>\n");
      out.write("    <body>\n");
      out.write("        <h2>Database SQL Load</h2>\n");
      out.write("        ");

            String dbname = "sob_grup_54";
            String schema = "ROOT";
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/" + dbname, "root", "root");
            Statement stmt = con.createStatement();
            
            out.println("<h3>--- Limpieza de Tablas y Secuencias ---</h3>");
            
            // =========================================================================
            // 1. LIMPIEZA DE DATOS (DELETE)
            // =========================================================================
            String cleanup[] = new String[]{
                // Es crucial borrar primero la tabla de colecciones (@ElementCollection)
                "DELETE FROM " + schema + ".MODEL_CAPABILITIES", 
                "DELETE FROM " + schema + ".IAS",
                "DELETE FROM " + schema + ".CUSTOMER",
               
            };
            
            for (String sql : cleanup) {
                try {
                    stmt.executeUpdate(sql);
                    out.println("<pre> -> OK: " + sql + "</pre>");
                } catch (SQLException e) {
                    // Ignorar errores de "tabla no existe" si es el primer despliegue
                    out.println("<pre class='error'> -> FALLO (Ignorado en limpieza): " + e.getMessage() + "</pre>");
                }
            }
            
            out.println("<h3>--- Inserción de Datos de Prueba ---</h3>");
            
            // =========================================================================
            // 2. INSERCIÓN DE DATOS DE PRUEBA
            // =========================================================================
            String data[] = new String[]{
                /* USUARIOS */
                // Usuario 1: sob (Obligatorio, sin último modelo consultado)
                "INSERT INTO " + schema + ".CUSTOMER (ID, USERNAME, PASSWORD, NAME, EMAIL, LASTCONSULTEDMODELID) VALUES (NEXT VALUE FOR CUSTOMER_GEN, 'sob', 'sob', 'Usuario Principal SOB', 'sob@urv.cat', NULL)",
                // Usuario 2: testuser (Para pruebas generales, consultó el modelo 1)
                "INSERT INTO " + schema + ".CUSTOMER (ID, USERNAME, PASSWORD, NAME, EMAIL, LASTCONSULTEDMODELID) VALUES (NEXT VALUE FOR CUSTOMER_GEN, 'testuser', 'test1234', 'Usuario de Prueba', 'test@test.com', 1)",
                
                /* MODELOS */
                // Modelo 1: Llama-3 (PÚBLICO)
                "INSERT INTO " + schema + ".IAS (ID, NAME, PROVIDER, ISPRIVATE, CONTEXTLENGTH, QUALITYINDEX) VALUES (NEXT VALUE FOR MODEL_GEN, 'Llama-3', 'Meta', 0, 8192, 0.75)",
                // Modelo 2: GPT-6 (PRIVADO)
                "INSERT INTO " + schema + ".IAS (ID, NAME, PROVIDER, ISPRIVATE, CONTEXTLENGTH, QUALITYINDEX) VALUES (NEXT VALUE FOR MODEL_GEN, 'GPT-6', 'OpenAI', 1, 300000, 0.98)",
                // Modelo 3: Claude-3 (PÚBLICO)
                "INSERT INTO " + schema + ".IAS (ID, NAME, PROVIDER, ISPRIVATE, CONTEXTLENGTH, QUALITYINDEX) VALUES (NEXT VALUE FOR MODEL_GEN, 'Claude-3', 'Anthropic', 0, 100000, 0.90)",
                
                /* CAPACIDADES */
                // Capacidades para Llama-3 (ID 1)
                "INSERT INTO " + schema + ".MODEL_CAPABILITIES (MODEL_ID, CAPABILITY) VALUES (1, 'chat-completion')",
                "INSERT INTO " + schema + ".MODEL_CAPABILITIES (MODEL_ID, CAPABILITY) VALUES (1, 'multilingual')",
                
                // Capacidades para GPT-6 (ID 2)
                "INSERT INTO " + schema + ".MODEL_CAPABILITIES (MODEL_ID, CAPABILITY) VALUES (2, 'coding')",
                "INSERT INTO " + schema + ".MODEL_CAPABILITIES (MODEL_ID, CAPABILITY) VALUES (2, 'reasoning')",
                
                // Capacidades para Claude-3 (ID 3)
                "INSERT INTO " + schema + ".MODEL_CAPABILITIES (MODEL_ID, CAPABILITY) VALUES (3, 'chat-completion')",
                "INSERT INTO " + schema + ".MODEL_CAPABILITIES (MODEL_ID, CAPABILITY) VALUES (3, 'vision')",
            };
            
            for (String datum : data) {
                if (stmt.executeUpdate(datum) <= 0) {
                    out.println("<span class='error'>Error inserting data: " + datum + "</span>");
                    return;
                }
                out.println("<pre> -> OK: " + datum + "<pre>");
            }
        
      out.write("\n");
      out.write("        <button onclick=\"window.location = '");
      out.print(request.getSession().getServletContext().getContextPath());
      out.write("'\">Go home</button>\n");
      out.write("    </body>\n");
      out.write("</html>\n");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          out.clearBuffer();
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
