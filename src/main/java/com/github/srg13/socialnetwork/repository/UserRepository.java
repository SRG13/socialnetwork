package com.github.srg13.socialnetwork.repository;

import com.github.srg13.socialnetwork.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String username);

}
