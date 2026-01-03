package deim.urv.cat.homework2.controller;

import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("logout")
@Controller
public class LogoutController {

    @Inject
    private HttpServletRequest request;

    @GET
    public String logout() {
        if (request.getSession(false) != null) {
            request.getSession().invalidate();
        }
        return "redirect:/models";
    }
}