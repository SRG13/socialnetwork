package com.github.srg13.socialnetwork.web;

import com.github.srg13.socialnetwork.AuthorizedUser;
import com.github.srg13.socialnetwork.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/profile")
@PreAuthorize("hasAuthority('USER')")
public class ProfileController {

    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String get(@AuthenticationPrincipal AuthorizedUser authUser, Model model) {
        model.addAttribute("user", userService.get(authUser.getId()));

        return "profile";
    }

    @PostMapping("/addProfileImg")
    public String addProfileImage(@AuthenticationPrincipal AuthorizedUser authUser, @RequestParam MultipartFile img)
            throws IOException {
        userService.saveProfileImage(authUser.getId(), img);

        return "redirect:/profile";
    }
}