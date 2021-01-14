package com.github.srg13.socialnetwork.util;

import com.github.srg13.socialnetwork.domain.User;

public class UserUtil {

    public static User prepareToSave(User user) {
        user.setEmail(user.getEmail().toLowerCase());

        return user;
    }
}