package deim.urv.cat.homework2.controller;

import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;

import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("logout") // CAMBIO 1: Minúscula para cumplir estándares web
@Controller
public class LogoutController {
    @Inject Logger log;
   
    @Context
    private HttpServletRequest request;
    
    @GET
    public String invalidate() {
        // Invalidate HTTP Session
        // CAMBIO 2: false para no crear sesión si no existe
        HttpSession session = request.getSession(false); 
        
        if (session != null) {
            Enumeration<String> attributes = session.getAttributeNames();
            while (attributes.hasMoreElements()) {
                String key = attributes.nextElement();
                Object obj  = session.getAttribute(key);
                log.log(Level.INFO, "Session attribute {0}:{1}", 
                        new Object [] { key, obj });
            }
            session.invalidate();
        }
        
        // CAMBIO 3 (SOLUCIÓN 404): 
        // Usamos "redirect:login" (sin barra / inicial).
        // Esto hace que la redirección sea relativa al path actual (/Web/),
        // llevando a /Web/login en lugar de intentar ir a la raíz incorrecta.
        return "redirect:login"; 
    }    
}