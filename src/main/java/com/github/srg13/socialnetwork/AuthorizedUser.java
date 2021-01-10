package com.github.srg13.socialnetwork;

import com.github.srg13.socialnetwork.domain.User;

public class AuthorizedUser extends org.springframework.security.core.userdetails.User {

    private final User user;

    public AuthorizedUser(User user) {
        super(user.getUsername(), user.getPassword(), user.isEnabled(), true, true,
                true, user.getRoles());
        this.user = user;
    }

    public Integer getId() {
        return user.getId();
    }
}