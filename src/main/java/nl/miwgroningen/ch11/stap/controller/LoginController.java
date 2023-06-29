package nl.miwgroningen.ch11.stap.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Author: Thijs Harleman
 * Created at 15:20 on 26 Jun 2023
 * Purpose: handle interactions with the login and logout page
 */

@Controller
public class LoginController {
    @GetMapping("/login")
    public String viewLoginPage() {
        return "general/login";
    }

    @GetMapping("/logout")
    public String viewLogoutPage() {
        return "general/logout";
    }
}
