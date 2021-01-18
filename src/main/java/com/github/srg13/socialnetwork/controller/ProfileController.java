package com.github.srg13.socialnetwork.controller;

import com.github.srg13.socialnetwork.model.User;
import com.github.srg13.socialnetwork.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

import static com.github.srg13.socialnetwork.util.WebUtil.getError;

@Controller
@RequestMapping("/profile")
@PreAuthorize("hasAuthority('USER')")
@AllArgsConstructor
public class ProfileController {
    private final UserService userService;

    @GetMapping
    public String get(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", userService.get(user.getId()));

        return "profile";
    }

    @PostMapping("/addProfileImg")
    public String addProfileImage(@AuthenticationPrincipal User user, @RequestParam MultipartFile img)
            throws IOException {
        userService.saveProfileImage(user.getId(), img);

        return "redirect:/profile";
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/registration")
    public String registration(
            @Valid User user,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = getError(bindingResult);
            model.addAttribute("errors", errors);
            model.addAttribute("user", user);

            return "registration";
        }

        userService.create(user);

        model.addAttribute("message",
                "You have successfully registered, check your email to find the activation link.");

        return "info";
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/registration/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        boolean isActivated = userService.activateUser(code);

        if (isActivated) {
            model.addAttribute("message", "Your account has been successfully activated.");
        } else {
            model.addAttribute("message", "Activation code is not found!");
        }

        return "info";
    }
}