package gr.aueb.cf.schoolapp.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(Principal principal, Authentication authentication) {
        if (principal == null) {
            return "login";
        }

        return "redirect:/teachers";

//        boolean isAdmin = authentication.getAuthorities()
//                .stream()
//                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
//        return isAdmin ? "redirect:/admin-dashboard" : "redirect:/employee-dashboard";
    }

    @GetMapping("/")
    public String root(Principal principal, Authentication authentication) {
        if (principal == null) {
            return "index";
        }

        return "redirect:/teachers";

//        boolean isAdmin = authentication.getAuthorities()
//                .stream()
//                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
//        return isAdmin ? "redirect:/admin-dashboard" : "redirect:/employee-dashboard";
    }
}
