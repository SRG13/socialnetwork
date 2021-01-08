package com.github.srg13.socialnetwork.web;

import com.github.srg13.socialnetwork.domain.User;
import com.github.srg13.socialnetwork.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/users")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    private final UserRepository userRepo;

    public AdminController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping
    public String getUsers(Model model) {
        model.addAttribute("users", userRepo.findAll());

        return "users";
    }

    @GetMapping("/{user}")
    public String getUser(@PathVariable User user, Model model) {
        model.addAttribute("user", user);

        return "userEdit";
    }

    @PostMapping("/{user}")
    public String editUser(@ModelAttribute User user) {
        userRepo.save(user);

        return "redirect:/users";
    }
}