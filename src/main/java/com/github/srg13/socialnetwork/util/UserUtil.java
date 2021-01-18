package com.github.srg13.socialnetwork.util;

import com.github.srg13.socialnetwork.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserUtil {
    public static User prepareToSave(User user, PasswordEncoder passwordEncoder) {
        String password = user.getPassword();
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(user.getEmail().toLowerCase());

        return user;
    }
}