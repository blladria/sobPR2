package deim.urv.cat.homework2.controller;

import deim.urv.cat.homework2.model.Model;
import deim.urv.cat.homework2.model.User;
import deim.urv.cat.homework2.service.ModelService;
import deim.urv.cat.homework2.service.UserService;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import java.util.List;
import java.util.Map;

@Controller
@Path("profile")
public class ProfileController {

    @Inject
    Models models;
    @Inject
    UserService userService;
    @Inject
    ModelService modelService;
    @Inject
    HttpSession session;

    @GET
    public String showProfile() {
        User sessionUser = (User) session.getAttribute("user");

        if (sessionUser == null) {
            return "redirect:/login";
        }

        User freshUser = userService.getUser(sessionUser.getUsername(), sessionUser.getPassword());

        if (freshUser != null) {
            models.put("userProfile", freshUser);

            String modelUrl = null;
            List<Map<String, String>> links = freshUser.getLinks();
            if (links != null) {
                for (Map<String, String> link : links) {
                    if ("model".equals(link.get("rel"))) {
                        modelUrl = link.get("href");
                        break;
                    }
                }
            }

            if (modelUrl != null) {
                try {
                    String idStr = modelUrl.substring(modelUrl.lastIndexOf("/") + 1);
                    Long modelId = Long.parseLong(idStr);
                    Model lastModel = modelService.find(modelId, freshUser.getUsername(), freshUser.getPassword());
                    models.put("lastModel", lastModel);
                } catch (Exception e) {
                    // Ignorar error de carga de modelo
                }
            }
            return "profile.jsp";
        }
        return "redirect:/login";
    }
}
