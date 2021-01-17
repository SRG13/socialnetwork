package com.github.srg13.socialnetwork.web;

import com.github.srg13.socialnetwork.domain.User;
import com.github.srg13.socialnetwork.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.Map;

import static com.github.srg13.socialnetwork.util.WebUtil.getError;

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

        userService.createOrUpdate(user);

        model.addAttribute("message",
                "You have successfully registered, check your email to find the activation link.");

        return "info";
    }

    @GetMapping("/activate/{code}")
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