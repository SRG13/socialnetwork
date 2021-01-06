package com.github.srg13.socialnetwork.web;

import com.github.srg13.socialnetwork.domain.Role;
import com.github.srg13.socialnetwork.domain.User;
import com.github.srg13.socialnetwork.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;

@Controller
@RequestMapping(value = "/registration")
public class RegistrationController {

    private final UserRepository userRepo;

    public RegistrationController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping
    public String registration() {
        return "registration";
    }

    @PostMapping
    public String registration(User user, Model model) {

        user.setRoles(Collections.singleton(Role.USER));
        user.setEnabled(true);
        userRepo.save(user);

        return "redirect:/login";
    }
}
