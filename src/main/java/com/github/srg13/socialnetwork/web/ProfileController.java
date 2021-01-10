package com.github.srg13.socialnetwork.web;

import com.github.srg13.socialnetwork.AuthorizedUser;
import com.github.srg13.socialnetwork.domain.User;
import com.github.srg13.socialnetwork.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/profile")
@PreAuthorize("hasAuthority('USER')")
public class ProfileController {

    @Value("${upload.path}")
    private String uploadPath;

    private final UserRepository userRepo;

    public ProfileController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping
    public String get(@AuthenticationPrincipal AuthorizedUser authUser, Model model) {
        model.addAttribute("user", userRepo.findById(authUser.getId()).orElseThrow(
                () -> new IllegalArgumentException("User with id=" + authUser.getId() + " not found")
        ));

        return "profile";
    }

    @PostMapping("/addProfileImg")
    public String addProfileImage(@AuthenticationPrincipal AuthorizedUser authUser, @RequestParam MultipartFile img)
            throws IOException {
        User user = userRepo.findById(authUser.getId()).orElseThrow(
                () -> new IllegalArgumentException("User with id=" + authUser.getId() + " not found")
        );

        if (img != null && !img.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + img.getOriginalFilename();

            img.transferTo(new File(uploadPath + "/" + resultFilename));

            user.setProfileImage(resultFilename);
            userRepo.save(user);
        }

        return "redirect:/profile";
    }
}