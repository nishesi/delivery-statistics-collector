package ru.itis.nishesi.statscollector.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.itis.nishesi.statscollector.model.dto.User;
import ru.itis.nishesi.statscollector.model.dto.UserDto;
import ru.itis.nishesi.statscollector.model.service.UserService;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/register")
    public String getRegistrationPage() {
        return "registration-page";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("userDto") UserDto userDto) {
        User user = userService.registerUser(userDto);
        return "redirect:/user";
    }

    @ModelAttribute
    void init(Model model) {
        model.addAttribute("userDto", new UserDto());
        model.addAttribute("problems", Map.of());
    }
}
