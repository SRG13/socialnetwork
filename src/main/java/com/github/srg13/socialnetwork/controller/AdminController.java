package com.github.srg13.socialnetwork.controller;

import com.github.srg13.socialnetwork.model.User;
import com.github.srg13.socialnetwork.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.Map;

import static com.github.srg13.socialnetwork.util.WebUtil.getError;

@Controller
@RequestMapping(value = "/admin")
@PreAuthorize("hasAuthority('ADMIN')")
@AllArgsConstructor
public class AdminController {
    private final UserService userService;

    @GetMapping("/users")
    public String getUsers(Model model) {
        model.addAttribute("users", userService.getAll());

        return "users";
    }

    @GetMapping("/users/{user}")
    public String getUser(@PathVariable User user, Model model) {
        model.addAttribute("user", user);

        return "userEdit";
    }

    @PostMapping("/users/{user}")
    public String editUser(
            @Valid @ModelAttribute User user,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = getError(bindingResult);
            model.addAttribute("errors", errors);

            return "userEdit";
        } else {
            userService.update(user);
        }

        return "redirect:/admin/users";
    }
}