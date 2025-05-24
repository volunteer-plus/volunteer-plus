package volunteer.plus.backend.api;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import volunteer.plus.backend.service.general.UserService;

@Controller
@RequestMapping(value = "/api/no-auth")
@RequiredArgsConstructor
public class Oauth2Controller {

    private final UserService userService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/home")
    @ResponseBody
    public String home(Model model) throws AuthenticationException {
        var user = userService.getCurrentUser();
        model.addAttribute("email", user.getEmail());
        return "home";
    }

}
