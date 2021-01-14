package com.github.srg13.socialnetwork.web;

import com.github.srg13.socialnetwork.domain.User;
import com.github.srg13.socialnetwork.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/registration")
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String registration() {
        return "registration";
    }

    @PostMapping
    public String registration(User user, Model model) {
        userService.createOrUpdate(user);

        return "redirect:/login";
    }
}
