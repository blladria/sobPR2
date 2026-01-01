<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import = "java.sql.*" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Database SQL Load</title>
    </head>
    <style>
        .error {
            color: red;
        }
        pre {
            color: green;
        }
    </style>
    <body>
        <h2>Database SQL Load</h2>
        <%
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
        %>
        <button onclick="window.location = '<%=request.getSession().getServletContext().getContextPath()%>'">Go home</button>
    </body>
</html>
