package ru.itis.nishesi.statscollector.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.itis.nishesi.statscollector.model.dto.User;
import ru.itis.nishesi.statscollector.model.service.UserService;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class SecurityController {
    private final UserService userService;

    @RequestMapping("/login/success")
    String processSuccess(HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());
        session.setAttribute("user", user);
        return "redirect:/";
    }
}
